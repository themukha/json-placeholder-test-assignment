package tech.themukha.placeholdertests.posts

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.logging.TestLogger
import tech.themukha.placeholdertests.utils.WordFrequencyCounter

class GetTopWordsFrequencyTest {

    @ParameterizedTest
    @ValueSource(ints = [10])
    @DisplayName("Top words frequency test")
    fun `Top words frequency test`(numberOfWords: Int) {
        val posts: List<PostDto> = PostsApi.`Get all posts`()!!
        TestLogger.info("Top $numberOfWords words frequency test")
        val topWords = WordFrequencyCounter.getTopWords(limit = numberOfWords, posts = posts)
        topWords.forEachIndexed { index, (word, count) ->
            TestLogger.info("${index + 1}. $word - $count")
        }
    }

}