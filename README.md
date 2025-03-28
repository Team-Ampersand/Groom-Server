# Groom

 GSM Room Booking System

---

## Commit Message Rule

| Type   | Meaning            |
|--------|--------------------|
| add    | 새로운 코드나 파일을 추가했을 때 |
| update | 기존의 코드를 수정했을 때     |
| fix    | 버그를 수정했을 때         |
| delete | 삭제한 사항이 있을 때       |
| chore  | 그 외 자잘한 작업을 했을 때   |
| docs   | 문서를 수정했을 때         |
| test   | 테스트 관련 사항을 수정했을 때  |
| merge  | 브랜치를 병합했을 때        |
| init   | 프로젝트를 초기화했을 때      |

### Example

```bash
docs: README 작성

README.md 파일을 작성했습니다
```
---

## Branch Rule

기본적으로 ``Git Flow``를 따르며, ``main``,`develop`,`feature`,`hotfix` 브랜치를 사용합니다.

| Branch  | Meaning                  | Merge Branch |
|---------|--------------------------|--------------|
| main    | 배포 가능한 상태로 유지하는 브랜치      | [가장 최신 버전]   |
| develop | 다음 버전을 개발하는 브랜치          | main         |
| feature | 기능을 개발하는 브랜치             | develop      |
| hotfix  | 배포 버전에서 발생한 버그를 수정하는 브랜치 | main         |


## Database Schema

<iframe width="600" height="336" src="https://www.erdcloud.com/p/gBu6AkAeoALCdRew2" frameborder="0" allowfullscreen></iframe>