# test_github_features

A small Java project for trying out GitHub workflows, branch protection, and status checks.

The build uses Gradle. Tests use JUnit. Formatting uses Spotless with [Google Java Format](https://github.com/google/google-java-format).

## Build

Java 21 is the language version. Gradle downloads a matching JDK if one is not already installed.

```shell
./gradlew test
./gradlew spotlessCheck
./gradlew spotlessApply
./gradlew run
```

`test` runs the JUnit suite. `spotlessCheck` fails when Java sources are not formatted. `spotlessApply` rewrites those files. `run` prints a greeting.

## Pre-commit hook

`.githooks/pre-commit` runs `./gradlew spotlessCheck` and stops the commit when formatting is wrong. Git does not version `.git/hooks`, so install the hook once in each clone:

```shell
./gradlew installGitHooks
```

After that, a commit with badly formatted Java is rejected. Format the code, stage it, and commit again:

```shell
./gradlew spotlessApply
git add .
git commit
```

## GitHub Actions

Two workflows run on every push and pull request:

| Workflow | File | Gradle task | Status check |
| --- | --- | --- | --- |
| JUnit | `.github/workflows/junit.yml` | `test` | `JUnit` |
| Spotless | `.github/workflows/spotless.yml` | `spotlessCheck` | `Spotless` |

They are separate workflows so each one can be required on its own in a branch protection rule. In the rule, the status check names to require are `JUnit` and `Spotless`. GitHub lists a check only after that workflow has run at least once on the repository.
