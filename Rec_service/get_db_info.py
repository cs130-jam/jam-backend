from dataclasses import dataclass


@dataclass
class AppProperties:
    username: str
    password: str


def getProps() -> AppProperties:
    with open("../src/main/resources/application.properties") as appProperties:
        username = None
        password = None

        for line in appProperties:
            parts = line.split("=")
            propName = parts[0].strip()
            if (propName == "spring.datasource.username"):
                username = parts[1].strip()
            elif (propName == "spring.datasource.password"):
                password = parts[1].strip()

        if username is None or password is None:
            print("FAILED TO PARSE application.properties FILE: MISSING FIELDS")
            exit(1)

        return AppProperties(username, password)
