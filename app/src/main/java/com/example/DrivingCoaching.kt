package com.example

/** Rule-based prototype feedback; never a vehicle condition or safety rating. */
object DrivingCoaching {
    data class Feedback(val title: String, val message: String, val limitations: List<String>)

    fun from(record: DriveJournal.Companion.Summary): Feedback {
        val limits = mutableListOf<String>()
        if (record.state == "기록 중") limits += "기록 중인 임시 결과입니다. 종료 후 다시 확인하세요."
        else if (record.state != "저장 완료") limits += "중단된 기록입니다. 수신된 구간만 분석했습니다."
        if (record.gaps > 0) limits += "수신 누락·오류가 있어 전체 주행을 대표하지 않을 수 있습니다."
        val (title, message) = when {
            record.count < 2 || record.analyzedSeconds <= 0 || record.maxKmh == null ->
                "분석 데이터 부족" to "연속 속도 데이터가 부족해 코칭을 만들 수 없습니다. 다음 테스트에서 수신 상태를 확인해 주세요."
            record.maxKmh == 0.0 ->
                "정지 상태 기록" to "수신된 속도가 모두 0입니다. 이 기록으로 주행 습관을 판단할 수 없습니다."
            record.accelerationEvents > 0 && record.brakingEvents > 0 ->
                "가속·감속 변화 확인" to "급가속 후보 ${record.accelerationEvents}회, 급감속 후보 ${record.brakingEvents}회가 검출됐습니다. 다음 주행에서는 주변 상황에 맞춰 속도를 부드럽게 조절하는 것을 목표로 해보세요."
            record.accelerationEvents > 0 ->
                "가속 변화 확인" to "급가속 후보 ${record.accelerationEvents}회가 검출됐습니다. 다음 주행에서는 출발과 재가속 시 속도를 점진적으로 높이는 것을 목표로 해보세요."
            record.brakingEvents > 0 ->
                "감속 변화 확인" to "급감속 후보 ${record.brakingEvents}회가 검출됐습니다. 다음 주행에서는 충분한 차간 거리를 유지하고 미리 감속하는 것을 목표로 해보세요. 필요한 긴급 제동은 우선입니다."
            else -> "기준 충족 후보 없음" to "분석 가능한 구간에서 급가속·급감속 기준을 충족한 후보가 검출되지 않았습니다. 짧은 변화나 제외된 구간은 반영되지 않으므로 안전 운전 판정은 아닙니다."
        }
        return Feedback(title, message, limits)
    }
}
