package org.evolutionsoftware.bookly.features.auth.createprofile

import java.util.Calendar

internal actual fun currentLocalDateMillisUtc(): Long {
    val calendar = Calendar.getInstance()
    val date =
        "${calendar.get(Calendar.YEAR).toString().padStart(4, '0')}-${
            (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        }-${calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}"
    return requireNotNull(date.toUtcBirthdateMillisOrNull())
}
