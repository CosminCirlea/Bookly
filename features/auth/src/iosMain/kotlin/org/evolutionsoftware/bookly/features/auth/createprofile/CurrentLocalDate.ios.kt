package org.evolutionsoftware.bookly.features.auth.createprofile

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

internal actual fun currentLocalDateMillisUtc(): Long {
    val components =
        NSCalendar.currentCalendar.components(
            unitFlags = NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = NSDate(),
        )
    val date =
        "${components.year.toString().padStart(4, '0')}-${
            components.month.toString().padStart(2, '0')
        }-${components.day.toString().padStart(2, '0')}"
    return requireNotNull(date.toUtcBirthdateMillisOrNull())
}
