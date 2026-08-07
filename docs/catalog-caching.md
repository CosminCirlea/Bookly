# Catalog caching and layering

Bookly is offline-first and runs on connections we do not control. The cache is the
source of truth for everything the UI renders; the network is only consulted when it
can tell us something we do not already know.

## What the backend must send

The traffic policy depends on the catalog list advertising a **content revision** per
book. `GET /api/books` should include one of the following on each item:

| Field | Type | Notes |
| --- | --- | --- |
| `content_version` | integer | Preferred. Bump whenever a book's pages change. |
| `updated_at` | string | Fallback, used only when `content_version` is absent. |

Both are optional in the client, so nothing breaks without them — but **until one is
sent, a book's pages are downloaded once and then never refreshed.** That is deliberate:
with no revision there is no evidence the cache is stale, and guessing would mean
spending traffic on every open. If you change a book's content, bump its
`content_version` or the change will not reach devices that already cached it.

Any value works as long as it changes when the content does; the client treats it as an
opaque string and only ever compares it for equality.

## The policy

`CatalogRefresh` (domain) states how much traffic a read may spend:

- **`CacheOnly`** — never touches the network.
- **`Automatic`** — the default, described below.
- **`Force`** — always re-fetches, for retry and pull-to-refresh. Falls back to cache
  if the request fails.

Under `Automatic`:

- **The book list** is revalidated **once per app session**. Opening the app refreshes
  it; navigating around afterwards costs nothing. An empty cache always reaches the
  network regardless, since there is nothing to serve.
- **A book's pages** are downloaded once, then served from disk **indefinitely**, until
  the list advertises a revision different from the one stored with the cached copy.
  Pages carry the bulk of the payload, so this is the saving that matters most.

### Failure behaviour

Offline-first means degrading rather than failing:

- A failed list refresh serves the cache, and only throws when the cache is empty.
- A failed page download serves the cached copy.
- A `404` for a book does **not** evict a copy already on the device — a child mid-book
  should not lose it because the catalog changed.
- Cancellation propagates as cancellation, and is never mistaken for a network error.

Concurrent list reads are serialised, so a burst of callers produces one request.

## Layers

```
presentation  features/*          Compose + MVI. Talks to use cases only.
    |
domain        services/*/domain   Models, repository interfaces, use cases, errors.
    |                             Knows nothing about Ktor, SQLDelight or DTOs.
    |
data          services/*/data     api/    remote data source + DTOs
                                  local/  cache interface + SQLDelight
                                  mapper/ DTO/row <-> domain
                                  error/  transport -> domain errors
                                  repository/ caching policy
```

Rules worth keeping:

- **The domain never sees a DTO or a database row.** Mapping happens in `data/mapper`.
- **The repository depends on interfaces** (`CatalogRemoteDataSource`, `CatalogCache`),
  not on Ktor or SQLDelight. This is what makes the caching policy unit-testable —
  see `CatalogRepositoryImplTest`, which covers the session policy, revision gating and
  every fallback path with in-memory fakes.
- **`data/error` is where transport failures become domain errors.** It deliberately
  does not live beside the repository, so the API client can use it without depending
  on the repository package.
- **The local data source deals only in rows.** The on-disk format for pages is private
  to the data layer.

## Cache database

The cache holds nothing that cannot be re-downloaded, so it ships **without SQL
migrations**. A schema change bumps `CATALOG_DATABASE_NAME` instead and the next launch
rebuilds the cache from the network. Bump it whenever `BookEntity.sq` changes shape —
otherwise existing installs will query columns that do not exist.

Detail rows for books that leave the catalog are deleted in the same transaction that
replaces the list, so the cache cannot grow without bound.

## Known gap: images

Page and cover art are fetched by Coil, which currently runs on its **default
`ImageLoader` with no explicit disk cache**. Images are much larger than the JSON, so
until this is configured the offline story is incomplete: a cached book may still show
blank art with no connection, and art may be re-downloaded across cold starts.

Configuring this needs a platform cache directory (`expect`/`actual`, an okio `Path`)
and a decision on whether to ignore server cache headers — reasonable here, since
content changes are signalled by the revision flag rather than by HTTP caching. It
should be verified on a real device on both platforms before being relied on.
