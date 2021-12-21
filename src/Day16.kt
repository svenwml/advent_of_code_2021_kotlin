fun main() {

    data class packet(var packet_version: Int, var packet_type_id: Int, var value: Long, var sub_packets: MutableList<packet>)
    data class process_output(val parsed_packet: packet, val input_bits: String)

    fun process_packets(input_bits: String) : process_output {

        var current_packet = packet(-1, -1, -1, mutableListOf())
        var input_bits_new = input_bits

        // Packet version.
        current_packet.packet_version = Integer.parseInt(input_bits_new.substring(0, 3), 2)
        input_bits_new = input_bits_new.drop(3)

        // Packet type id.
        current_packet.packet_type_id = Integer.parseInt(input_bits_new.substring(0, 3), 2)
        input_bits_new = input_bits_new.drop(3)

        // Check if we have a literal packet.
        if (current_packet.packet_type_id == 4) {

            var last_group_parsed = false

            var literal_part_values = mutableListOf<Long>()

            while (!last_group_parsed) {
                // Each group has five bits.

                // The first bit signalizes if this is the last group.
                last_group_parsed = (0 == Integer.parseInt(input_bits_new.substring(0, 1), 2))
                input_bits_new = input_bits_new.drop(1)

                val group_value = Integer.parseInt(input_bits_new.substring(0, 4), 2).toLong()
                input_bits_new = input_bits_new.drop(4)

                literal_part_values.add(group_value)
            }

            // Calculate group result.
            current_packet.value = 0L
            for (literal_part_value_index in 0 until literal_part_values.size) {
                current_packet.value += literal_part_values[literal_part_value_index].shl((literal_part_values.size - 1 - literal_part_value_index) * 4)
            }

            return process_output(current_packet, input_bits_new)
        }
        else
        {
            // Any other packet type -> operator packet.
            val length_type_id = Integer.parseInt(input_bits_new.substring(0, 1), 2)
            input_bits_new = input_bits_new.drop(1)

            if (length_type_id == 0) {
                // The next 15 bits signalize how many bits with sub-packets will follow.
                var sub_packet_bits = Integer.parseInt(input_bits_new.substring(0, 15), 2)
                input_bits_new = input_bits_new.drop(15)

                while(sub_packet_bits > 6)
                {
                    val process_output = process_packets(input_bits_new)
                    var bits_processed = input_bits_new.length - process_output.input_bits.length
                    input_bits_new = input_bits_new.drop(bits_processed)
                    sub_packet_bits -= bits_processed
                    current_packet.sub_packets.add(process_output.parsed_packet)
                }
            }
            else if (length_type_id == 1)
            {
                // The next 11 bits signalize how many sub-packets are contained in this packet.
                val sub_packet_count = Integer.parseInt(input_bits_new.substring(0, 11), 2)
                input_bits_new = input_bits_new.drop(11)

                for(i in 0 until sub_packet_count)
                {
                    val process_output = process_packets(input_bits_new)
                    input_bits_new = process_output.input_bits
                    current_packet.sub_packets.add(process_output.parsed_packet)
                }
            }
        }

        return process_output(current_packet, input_bits_new)
    }

    fun compute_values(input_packet: packet): Long
    {
        when (input_packet.packet_type_id) {
            0 -> {
                // Sum.
                var sum = 0L

                for(sub_packet_index in 0 until input_packet.sub_packets.size)
                {
                    sum += compute_values(input_packet.sub_packets[sub_packet_index])
                }

                return sum
            }
            1 -> {
                // Product.
                var product = 1L

                for(sub_packet_index in 0 until input_packet.sub_packets.size)
                {
                    product *= compute_values(input_packet.sub_packets[sub_packet_index])
                }

                return product
            }
            2 -> {
                // Minimum.
                var minimum = Long.MAX_VALUE

                for(sub_packet_index in 0 until input_packet.sub_packets.size)
                {
                    val val_tmp = compute_values(input_packet.sub_packets[sub_packet_index])

                    if(val_tmp < minimum)
                    {
                        minimum = val_tmp
                    }
                }

                return minimum
            }
            3 -> {
                // Maximum.
                var maximum = Long.MIN_VALUE

                for(sub_packet_index in 0 until input_packet.sub_packets.size)
                {
                    val val_tmp = compute_values(input_packet.sub_packets[sub_packet_index])

                    if(val_tmp > maximum)
                    {
                        maximum = val_tmp
                    }
                }

                return maximum
            }
            4 -> {
                return input_packet.value
            }
            5 -> {
                // Greater than.
                var value = 0L

                if(compute_values(input_packet.sub_packets[0]) > compute_values(input_packet.sub_packets[1]))
                {
                    value = 1L
                }

                return value
            }
            6 -> {
                // Less than.
                var value = 0L

                if(compute_values(input_packet.sub_packets[0]) < compute_values(input_packet.sub_packets[1]))
                {
                    value = 1L
                }

                return value
            }
            7 -> {
                // Equal to.
                var value = 0L

                if(compute_values(input_packet.sub_packets[0]) == compute_values(input_packet.sub_packets[1]))
                {
                    value = 1L
                }

                return value
            }
        }

        return -1
    }

    fun get_version_sum(input_packet: packet) : Long
    {
        var version_sum = 0L

        for(sub_packet_index in 0 until input_packet.sub_packets.size)
        {
            version_sum += get_version_sum(input_packet.sub_packets[sub_packet_index])
        }

        version_sum += input_packet.packet_version

        return version_sum
    }

    fun parse_input_bitstream(input: String): String
    {
        var input_bits = ""

        for (hex_value in input) {
            val binary_value = Integer.toBinaryString(hex_value.toString().toInt(radix = 16)).padStart(4, '0')
            input_bits += binary_value
        }

        return input_bits
    }

    fun part1(input: String): Long {

        val input_bits = parse_input_bitstream(input)

        val process_output = process_packets(input_bits)

        val version_sum = get_version_sum(process_output.parsed_packet)

        return version_sum
    }

    fun part2(input: String): Long {

        val input_bits = parse_input_bitstream(input)

        val process_output = process_packets(input_bits)

        val result = compute_values(process_output.parsed_packet)

        return result
    }

    val testInput1 = readInput("Day16_test_1")

    check(part1(testInput1[0]) == 16L)
    check(part1(testInput1[1]) == 12L)
    check(part1(testInput1[2]) == 23L)
    check(part1(testInput1[3]) == 31L)

    val testInput2 = readInput("Day16_test_2")

    check(part2(testInput2[0]) == 3L)
    check(part2(testInput2[1]) == 54L)
    check(part2(testInput2[2]) == 7L)
    check(part2(testInput2[3]) == 9L)
    check(part2(testInput2[4]) == 1L)
    check(part2(testInput2[5]) == 0L)
    check(part2(testInput2[6]) == 0L)
    check(part2(testInput2[7]) == 1L)

    val input = readInput("Day16")

    println(part1(input[0]))
    println(part2(input[0]))
}