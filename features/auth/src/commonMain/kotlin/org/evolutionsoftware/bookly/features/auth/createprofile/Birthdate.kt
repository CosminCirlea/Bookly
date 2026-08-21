package org.evolutionsoftware.bookly.features.auth.createprofile

private const val MILLIS_PER_DAY = 86_400_000L

internal fun Long.toIsoBirthdate(): String {
    var days = floorDiv(this, MILLIS_PER_DAY) + 719_468L
    val era = floorDiv(days, 146_097L)
    val dayOfEra = days - era * 146_097L
    val yearOfEra =
        (dayOfEra - dayOfEra / 1_460L + dayOfEra / 36_524L - dayOfEra / 146_096L) / 365L
    var year = yearOfEra + era * 400L
    val dayOfYear = dayOfEra - (365L * yearOfEra + yearOfEra / 4L - yearOfEra / 100L)
    val monthPart = (5L * dayOfYear + 2L) / 153L
    val day = dayOfYear - (153L * monthPart + 2L) / 5L + 1L
    val month = monthPart + if (monthPart < 10L) 3L else -9L
    year += if (month <= 2L) 1L else 0L

    return "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${
        day.toString().padStart(2, '0')
    }"
}

internal fun String.toUtcBirthdateMillisOrNull(): Long? {
    val parts = split("-")
    if (parts.size != 3) return null

    val year = parts[0].toLongOrNull() ?: return null
    val month = parts[1].toLongOrNull() ?: return null
    val day = parts[2].toLongOrNull() ?: return null
    if (month !in 1L..12L || day !in 1L..31L) return null

    val adjustedYear = year - if (month <= 2L) 1L else 0L
    val era = floorDiv(adjustedYear, 400L)
    val yearOfEra = adjustedYear - era * 400L
    val monthPart = month + if (month > 2L) -3L else 9L
    val dayOfYear = (153L * monthPart + 2L) / 5L + day - 1L
    val dayOfEra = yearOfEra * 365L + yearOfEra / 4L - yearOfEra / 100L + dayOfYear
    val millis = (era * 146_097L + dayOfEra - 719_468L) * MILLIS_PER_DAY

    return millis.takeIf { it.toIsoBirthdate() == this }
}

private fun floorDiv(
    dividend: Long,
    divisor: Long,
): Long {
    val quotient = dividend / divisor
    val remainder = dividend % divisor
    return if (remainder != 0L && (dividend xor divisor) < 0L) quotient - 1L else quotient
}
