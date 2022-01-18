- **사용 스택** : JAVA, CM(Client-Server Communication Library)
- **개발 기간** : 2021년 3월 26일 ~ 6월 7일
- **시연 영상** : [https://youtu.be/Jx-a1zcKmV0](https://youtu.be/Jx-a1zcKmV0)
- **프로젝트 내용 요약**
    - 건국대 CCS 연구실의 CM 미들웨어 라이브러리에서 제공하는 Message Protocol과
    직접 설계한 Message Protocol을 둘 다 활용하여 채팅 프로그램 개발하는 팀 프로젝트 진행
    - 주제 : 종단간 암호화를 적용한 기업용 온라인 메신저 (회의룸)
- **제작 문서**
    
    [소켓 통신 기반 채팅 프로그램 문서](https://jasonyoo95.notion.site/47cf8f23907f45e584b32c33a5eed200)
    
- **어려웠던 점**
    - 라이브러리의 크기과 기능이 매우 거대하고 방대해서, 동작 메커니즘을 전체를 이해하는 것이 불가능했고 필요한 부분만 이해해야 하는 과정에서, 가이드 문서만으로 커버되지 않는 부분은 장님이 코끼리 만지듯 스스로 이해해야 했는데, 통신과 관련된 Control Flow와 Data Flow를 분석하고 디버깅하는 데에 오랜 시간이 걸렸습니다.
    - 라이브러리 내 객체 간 의존 관계가 복잡하여, 특정 객체가 다음 동작을 수행하기 위해 사전에 알고 있어야 하는 데이터가 무엇인지를 고려하여 메서드 호출 순서를 설계해야 했습니다. 또한 이러한 순서를 고려하여 데이터의 통신 시 담아보내줘야 할 데이터의 종류를 정해줘야 했고, 특히 여러 개의 통신 이벤트가 동시에 일어날 때, 이벤트의 처리 순서를 적절하게 설계해야 했습니다.
    - 라이브러리가 제공하는 broadcast와 multicast, unicast의 지원 범위와 동작 방식을 고려하여 응용을 추가하는 것이 어려웠습니다.
