package com.marina.ruiz.globetrotting.data.network.chatGpt.model

data class Choice(
    val text: String
)

data class ChatGptResponse(
    val choices: List<Choice>
) {
    fun asApiModel(): ChatGptApiModel {
        return ChatGptApiModel(
            choices[0].text
        )
    }
}
