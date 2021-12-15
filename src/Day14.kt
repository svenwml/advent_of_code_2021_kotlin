fun main() {

    fun compute_polymers(input: List<String>, number_of_steps: Int) : Long
    {
        val polymer_template = input[0]

        // Build insertion rules and pair counts map.

        val insertion_rules = mutableMapOf<String, String>()
        var pair_counts = mutableMapOf<String, Long>()
        var pair_counts_next_step = mutableMapOf<String, Long>()

        for(input_line_index in 2 until input.size)
        {
            val insertion_rule_split = input[input_line_index].split(" -> ")
            insertion_rules.put(insertion_rule_split[0], insertion_rule_split[1])
            pair_counts.put(insertion_rule_split[0], 0)
            pair_counts_next_step.put(insertion_rule_split[0], 0)
        }

        // Add initial pair counts.
        for(char_index_pair_begin in 0 until polymer_template.length - 1)
        {
            val pair = polymer_template[char_index_pair_begin].toString() + polymer_template[char_index_pair_begin + 1].toString()
            pair_counts[pair] = pair_counts[pair]!! + 1
            pair_counts_next_step[pair] = 0
        }

        for(step in 0 until number_of_steps)
        {
            for(pair in pair_counts.keys)
            {
                if(pair_counts[pair]!! > 0)
                {
                    val resulting_pair_0 = pair[0] + insertion_rules[pair]!!
                    val resulting_pair_1 = insertion_rules[pair]!! + pair[1]

                    pair_counts_next_step[resulting_pair_0] = pair_counts_next_step[resulting_pair_0]!! + pair_counts[pair]!!
                    pair_counts_next_step[resulting_pair_1] = pair_counts_next_step[resulting_pair_1]!! + pair_counts[pair]!!

                    pair_counts[pair] = 0
                }
            }

            pair_counts.putAll(pair_counts_next_step)

            for(key in pair_counts_next_step.keys)
            {
                pair_counts_next_step[key] = 0
            }
        }

        // Count elements.
        // Note: In this first step, we will count each element in the pairs list, so we will get twice the real
        // count for each element. We will correct that later.
        var element_counts = mutableMapOf<Char, Long>()

        for(pair_count in pair_counts)
        {
            for(pair_index in 0 until 2)
            {
                if (element_counts[pair_count.key[pair_index]] == null)
                {
                    element_counts.put(pair_count.key[pair_index], pair_count.value)
                }
                else
                {
                    element_counts[pair_count.key[pair_index]] = element_counts[pair_count.key[pair_index]]!! + pair_count.value
                }
            }
        }

        // Divide element counts by 2 (pairs -> elements).
        for(element in element_counts.keys)
        {
            element_counts[element] = ((element_counts[element]!! / 2.0) + 0.5).toLong()
        }

        // Get most and least common element.
        val max = element_counts.maxByOrNull { it.value }!!.value
        val min = element_counts.minByOrNull { it.value }!!.value

        return max-min
    }

    fun part1(input: List<String>) : Long {

        return compute_polymers(input, 10)
    }

    fun part2(input: List<String>) : Long {

        return compute_polymers(input, 40)
    }

    val testInput = readInput("Day14_test")

    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")

    println(part1(input))
    println(part2(input))
}