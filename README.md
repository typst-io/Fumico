<div align="center">
    <h1>fumico</h1>
    <p>
        A script language allows you to do <u>fu</u>nctional <u>mi</u>necraft <u>co</u>ding.
    </p>
    <hr />
    <img alt="build status" src="https://img.shields.io/github/workflow/status/typecraftio/Fumico/Build?style=for-the-badge" />
    <img alt="language" src="https://img.shields.io/github/languages/top/typecraftio/Fumico?style=for-the-badge" />
    <img alt="license" src="https://img.shields.io/github/license/typecraftio/Fumico?style=for-the-badge" />
</div>

## 철학

### 언어

- 불변 데이터 구조만
- 강력한 타입 시스템 (HKT니 GADT니 하는 것들 다 구현해보자)

### 표준 라이브러리

- 기능은 풍부하되, 모듈은 작게
- 모든 것은 합성 가능하게

## 대충 필요한 거 같은 기능들

- do notation
- monad
- ufcs

## 결정된 것

- 함수는 모두 단인자 함수이다. **예외란 없다**
- 타입 시스템은 힌들리-밀너를 바탕으로
