# GitHub Actions: Automated API Test Workflow

This repository uses GitHub Actions to automatically run API tests using Maven and JUnit.

## Workflow Details

- **Location:** `.github/workflows/api-tests.yml`
- **Triggers:**
  - On every push to `main` and `first-api-branch`
  - Every Friday at midnight (UTC)

## How It Works

1. **Checkout code:** Uses the latest code from the repository.
2. **Set up JDK 17:** Ensures the correct Java version is used for testing.
3. **Cache Maven packages:** Speeds up builds by caching dependencies.

4. **Build and test:** Runs `mvn clean test` to execute all tests.
5. **Upload Allure Results:** After tests, the workflow uploads the `allure-results` directory as an artifact for download and review.

## Test Reports

After each run, Allure results are available as downloadable workflow artifacts in the GitHub Actions run summary.

- To change the schedule, edit the `cron` value in `.github/workflows/api-tests.yml`.
- To add more branches or steps, update the workflow file as needed.

## Example Workflow File (with Allure Results Upload)

```yaml
name: Run API Tests
on:
  push:
    branches:
      - main
      - first-api-branch
  schedule:
    - cron: '0 0 * * 5'
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
      - name: Cache Maven packages
        uses: actions/cache@v4
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-m2-
      - name: Build and test
        run: mvn clean test
```

## More Information

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/)
- [JUnit Documentation](https://junit.org/)
