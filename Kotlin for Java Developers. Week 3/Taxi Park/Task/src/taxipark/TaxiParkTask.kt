package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val filter = allDrivers.filter({ driver ->
        trips.none { trip -> trip.driver == driver }
    })

    return filter.toSet()
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.count { trip -> passenger in trip.passengers } >= minTrips
    }.toSet()


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers.filter { passenger ->
        trips.count { trip ->
               trip.driver == driver
            && passenger in trip.passengers
        } > 1
    }.toSet()


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers.filter { passenger ->
        val (tripsWithDiscount, tripsWithoutDiscount) = trips
            .filter { trip -> passenger in trip.passengers }
            .partition { trip -> trip.discount != null && trip.discount > 0 }

        tripsWithDiscount.size > tripsWithoutDiscount.size
    }.toSet()


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
//    if ( trips.isNullOrEmpty() )
//        return null

    return trips
        .groupBy({ trip ->
            val rangeStart = (trip.duration / 10) * 10
            rangeStart..rangeStart + 9
        })
        .maxBy { (_, tripsByRange) -> tripsByRange.size }
        ?.key

}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isNullOrEmpty())
        return false

    val totalCost = trips.sumByDouble(Trip::cost)
    val eightyPercentCost = totalCost * 0.8

    val twentyPercentOfDrivers = (allDrivers.size * 0.2).toInt()

    val totalIncomeByTwentyPercentOfDrivers = allDrivers
        .map { driver ->
            trips
                .filter { trip -> trip.driver == driver }
                .sumByDouble(Trip::cost)
        }
        .sorted() // sort by total cost
        .takeLast(twentyPercentOfDrivers) // total costs by 20% drivers with most income
        .sumByDouble { cost -> cost } // sum these total costs by 20% best drivers

    return totalIncomeByTwentyPercentOfDrivers >= eightyPercentCost
}