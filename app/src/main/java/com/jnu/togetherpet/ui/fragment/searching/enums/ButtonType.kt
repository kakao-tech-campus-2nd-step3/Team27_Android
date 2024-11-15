package com.jnu.togetherpet.ui.fragment.searching.enums

// 같이 찾기 버튼 목록
enum class ButtonType(var displayText: String) {
    MISSING("실종 정보"),
    REPORT("제보 정보"),
    MyPET("내 반려 동물");   //기본 값

    fun setPetName(petName: String): ButtonType {
        return MyPET.apply { displayText = petName }
    }
}