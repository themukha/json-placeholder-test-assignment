package tech.themukha.placeholdertests.posts

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tech.themukha.placeholdertests.api.PostsApi
import tech.themukha.placeholdertests.dto.PostDto
import tech.themukha.placeholdertests.flow.TestFlow
import tech.themukha.placeholdertests.logging.TestLogger
import tech.themukha.placeholdertests.utils.WordFrequencyCounter

@DisplayName("Get Top Words Frequency Test")
class GetTopWordsFrequencyTest {

    @ParameterizedTest(name = "Getting posts and count words in bodies")
    @ValueSource(ints = [10])
    @DisplayName("Top words frequency test")
    fun `Top words frequency test`(numberOfWords: Int) {

        TestFlow()
            .step("Top $numberOfWords words frequency:") {
                val posts: List<PostDto> = PostsApi.`Get all posts`()!!
                val topWords = WordFrequencyCounter.getTopWords(limit = numberOfWords, posts = posts)
                topWords.forEachIndexed { index, (word, count) ->
                    TestLogger().info("${index + 1}. $word - $count")
                }
            }
    }

}