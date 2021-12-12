fun main() {

    data class transit(val start_point: String, val end_point: String)

    fun get_number_of_routes(input: List<String>, allow_one_small_cave_to_be_visited_twice: Boolean) : Int
    {
        var transits = mutableListOf<transit>()

        for(line in input)
        {
            val split_line = line.split("-")
            if(split_line[0] == "end" || split_line[1] == "start")
            {
                // "start" can never be the destination and "end" can never be the start, swap the entry.
                transits.add(transit(split_line[1],split_line[0]))
            }
            else
            {
                transits.add(transit(split_line[0],split_line[1]))
            }
        }

        // Transits are bidirectional, so add all transits again with swapped start point and end point.

        val transits_size = transits.size

        for(transit_index in 0 until transits_size)
        {
            // Exceptions: "start" can never be the destination and "end" can never be the start.
            if(transits[transit_index].start_point != "start" && transits[transit_index].end_point != "end")
            {
                transits.add(transit(transits[transit_index].end_point, transits[transit_index].start_point))
            }
        }

        var routes = mutableListOf<MutableList<String>>()

        // Each "start" point can start a new route.
        for(start_transit in transits)
        {
            if (start_transit.start_point == "start") {
                var route = mutableListOf<String>()

                route.add(start_transit.start_point)
                route.add(start_transit.end_point)

                routes.add(route)
            }
        }

        do
        {
            var new_route_points_found = false

            var routes_size = routes.size

            for (route_index in 0 until routes_size)
            {
                var transit_candidates = mutableListOf<transit>()

                for (transit_candidate in transits)
                {
                    if (transit_candidate.start_point == routes[route_index][routes[route_index].size - 1])
                    {
                        // Check if this transit is possible.

                        var transit_candidate_possible = true

                        // Capital lettered caves can be entered multiple times, so they do not have to be checked.
                        if (!transit_candidate.end_point.matches("[A-Z]+".toRegex()))
                        {
                            for (route_point in routes[route_index])
                            {
                                if (transit_candidate.end_point == route_point)
                                {
                                    // Non-capital lettered caves can only be entered one time per route.
                                    // Exception: For part two, up to two visits for one non-capital lettered
                                    // cave per route is allowed.
                                    if(allow_one_small_cave_to_be_visited_twice)
                                    {
                                       // Check how often we visited each cave.
                                        val occurencies = routes[route_index].groupingBy { it }.eachCount()

                                        for(occurrency in occurencies)
                                        {
                                            if(!occurrency.key.matches("[A-Z]+".toRegex()) && occurrency.value > 1)
                                            {
                                                transit_candidate_possible = false
                                                break
                                            }
                                        }

                                        if(!transit_candidate_possible)
                                        {
                                            break
                                        }
                                    }
                                    else
                                    {
                                        transit_candidate_possible = false
                                        break
                                    }
                                }
                            }
                        }

                        if (transit_candidate_possible)
                        {
                            transit_candidates.add(transit_candidate)
                        }
                    }
                }

                // Add the current route to the route list for all new occurring possible routes.

                var route_before = routes[route_index].toList()

                for(i in 0 until transit_candidates.size)
                {
                    new_route_points_found = true

                    var new_route_index = route_index
                    if(i > 0)
                    {
                        // This will be a new possible route.
                        routes.add(route_before.toMutableList())
                        new_route_index = routes.size - 1
                    }

                    routes[new_route_index].add(transit_candidates[i].end_point)
                }
            }

        } while(new_route_points_found)

        val valid_routes = mutableListOf<MutableList<String>>()

        for(route in routes)
        {
            if (route.lastOrNull() == "end" && route.first() == "start")
            {
                valid_routes.add(route)
            }
        }

        return valid_routes.size
    }

    fun part1(input: List<String>): Int {

        return get_number_of_routes(input, false)
    }

    fun part2(input: List<String>): Int {

        return get_number_of_routes(input, true)
    }

    val testInput1 = readInput("Day12_test_1")
    val testInput2 = readInput("Day12_test_2")
    val testInput3 = readInput("Day12_test_3")

    check(part1(testInput1) == 10)
    check(part1(testInput2) == 19)
    check(part1(testInput3) == 226)

    check(part2(testInput1) == 36)
    check(part2(testInput2) == 103)
    check(part2(testInput3) == 3509)

    val input = readInput("Day12")

    println(part1(input))
    println(part2(input))
}