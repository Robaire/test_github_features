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

| Workflow | File | When | Gradle task | Status check |
| --- | --- | --- | --- | --- |
| JUnit | `.github/workflows/junit.yml` | push and pull request | `test` | `JUnit` |
| Spotless | `.github/workflows/spotless.yml` | push and pull request | `spotlessCheck` | `Spotless` |
| Spotless Apply | `.github/workflows/spotless-apply.yml` | push | `spotlessApply` | `Spotless Apply` |

JUnit and Spotless are separate workflows so each one can be required on its own in a branch protection rule. GitHub lists a check only after that workflow has run at least once on the repository.

Spotless Apply formats only the Java files changed by the push. It compares against the branch tip from before the push, or against the default branch for a newly pushed branch. If Spotless changes anything, the workflow commits those files with the message `Update formatting` and pushes that commit.

That push uses the workflow's `GITHUB_TOKEN`. GitHub does not start new workflow runs from that token, so the formatting commit does not run JUnit or Spotless again. On an already formatted push, the job finishes without a new commit.
