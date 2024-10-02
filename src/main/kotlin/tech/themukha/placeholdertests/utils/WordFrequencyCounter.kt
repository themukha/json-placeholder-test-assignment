package tech.themukha.placeholdertests.utils

import org.junit.jupiter.api.Assertions.assertEquals

object WordFrequencyCounter {

    /**
     * @param posts список тел постов ($.body)
     * @param limit количество самых встречающихся слов, которые будут возвращены в результате
     * @return список пар самых часто встречающихся слов (слово, количество вхождений в постах).
     * Используем именно список пар, а не Map, так как список гарантирует корректный порядок топ-10 слов.
     * */
    fun getTopWords(posts: List<String>, limit: Int = 10): List<Pair<String, Int>> {
        val wordCounts: MutableMap<String, Int> = mutableMapOf()

        for (post in posts) {
            val words = post.lowercase().split(Regex("\\s+")) // используем регулярку для разбиения строки на слова на тот случай, если встречаются несколько пробелов подряд
            for (word in words) {
                wordCounts[word] = wordCounts.getOrDefault(word, 0) + 1
            }
        }
        return wordCounts.entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key to it.value }
    }
}

fun main() {
    val postsBodies = listOf(
        "This is a test post",
        "This is another test post with some more words",
        "And yet another post for testing purposes"
    )

    val expectedTopWords = listOf(
        "post" to 3,
        "this" to 2,
        "is" to 2,
        "test" to 2,
        "another" to 2,
        "a" to 1,
        "with" to 1,
        "some" to 1,
        "more" to 1,
        "words" to 1
    )

    val actualTopWords = WordFrequencyCounter.getTopWords(postsBodies)

    assertEquals(expectedTopWords, actualTopWords)
    println("Top ${actualTopWords.size} words:")
    actualTopWords.forEachIndexed { index, (word, count) ->
        println("${index + 1}. $word - $count")
    }
}