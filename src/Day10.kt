import java.lang.Exception
import java.util.ArrayDeque

fun main() {

    data class parse_character_return(val wrong_character_detected: Boolean, val bracket_stack: ArrayDeque<Char>)

    fun parse_character(character: Char, bracket_stack: ArrayDeque<Char>) : parse_character_return
    {
        var wrong_character_detected = false

        if(character == '(' || character == '[' || character == '{' || character == '<')
        {
            bracket_stack.push(character)
        }
        else
        {
            val expected_counterpart = bracket_stack.pop()

            when(expected_counterpart)
            {
                '(' ->
                {
                    if(character != ')')
                    {
                        wrong_character_detected = true
                    }
                }
                '[' ->
                {
                    if(character != ']')
                    {
                        wrong_character_detected = true
                    }
                }
                '{' ->
                {
                    if(character != '}')
                    {
                        wrong_character_detected = true
                    }
                }
                '<' ->
                {
                    if(character != '>')
                    {
                        wrong_character_detected = true
                    }
                }
            }
        }

        return parse_character_return(wrong_character_detected, bracket_stack)
    }

    fun part1(input: List<String>): Int {

        var bracket_stack = ArrayDeque<Char>()

        var points = 0

        for(line in input)
        {
            for(character in line)
            {
                var parse_character_return = parse_character(character, bracket_stack)

                bracket_stack = parse_character_return.bracket_stack

                if(parse_character_return.wrong_character_detected)
                {
                    when(character)
                    {
                        ')' -> points += 3
                        ']' -> points += 57
                        '}' -> points += 1197
                        '>' -> points += 25137
                    }
                }
            }
        }

        return points
    }

    fun part2(input: List<String>): Long {

        var bracket_stack = ArrayDeque<Char>()

        var incomplete_lines = mutableListOf<String>()

        for(line in input)
        {
            var wrong_character_detected = false

            for(character in line)
            {
                val parse_character_return = parse_character(character, bracket_stack)

                bracket_stack = parse_character_return.bracket_stack
                wrong_character_detected = parse_character_return.wrong_character_detected

                if(wrong_character_detected)
                {
                     break
                }
            }

            if(!wrong_character_detected)
            {
                incomplete_lines.add(line)
            }
        }

        // Now we have the incomplete lines.

        bracket_stack.clear()

        var scores = mutableListOf<Long>()

        for(line in incomplete_lines)
        {
            // We know that the line is not corrupted, but incomplete.

            for(character in line)
            {
                if(character == '(' || character == '[' || character == '{' || character == '<')
                {
                    bracket_stack.push(character)
                }
                else
                {
                    bracket_stack.pop()
                }
            }

            // Now let's see what is remaining in the stack and calculate the score.

            var score = 0.toLong()

            while(true)
            {
                var character = ' '

                try {
                    character = bracket_stack.pop()
                }
                catch (e: Exception)
                {
                    break
                }

                score *= 5

                when(character)
                {
                    '(' -> score += 1
                    '[' -> score += 2
                    '{' -> score += 3
                    '<' -> score += 4
                }
            }

            scores.add(score)
        }

        // Sort scores and return the middle score as result.
        return scores.sorted()[scores.size / 2]
    }

    val testInput = readInput("Day10_test")

    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957.toLong())

    val input = readInput("Day10")

    println(part1(input))
    println(part2(input))
}