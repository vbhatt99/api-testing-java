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

4. **Build and test:** Runs `mvn clean test` with secure environment variables to execute all tests.
5. **Upload Allure Results:** After tests, the workflow uploads the `allure-results` directory as an artifact for download and review.

## Environment Variables

The workflow now includes secure environment variables for testing:

```yaml
env:
  # Database Configuration
  DB_USERNAME: sa
  DB_PASSWORD: ci_test_password_123
  # H2 Console Configuration (disabled for CI)
  H2_CONSOLE_ENABLED: false
  # Logging Configuration
  LOG_LEVEL: INFO
  SPRING_WEB_LOG_LEVEL: WARN
  HIBERNATE_SQL_LOG_LEVEL: WARN
  HIBERNATE_BINDER_LOG_LEVEL: WARN
  # Management Endpoints Configuration
  MANAGEMENT_ENDPOINTS: health
  HEALTH_SHOW_DETAILS: when-authorized
```

These variables ensure secure configuration and proper test execution in the CI environment.

## Test Reports

After each run:

- **Artifacts:** Allure results (`allure-results`) and the generated HTML report (`allure-report`) are available as downloadable workflow artifacts in the GitHub Actions run summary.
- **Online Viewing:** The workflow automatically deploys the latest Allure HTML report to GitHub Pages (`gh-pages` branch). Only the most recent report is available online, and it is overwritten with each run.

### How to View the Latest Allure Report Online

1. Go to your repository’s **Settings > Pages**.
2. Set the source branch to `gh-pages` and folder to `/` (root).
3. Save the settings. GitHub will provide a public URL (e.g., `https://<your-username>.github.io/api-testing-java/`).
4. Open the URL in your browser to view the latest Allure report.

> **Note:** The report is public if your repository is public. Review your test results for sensitive information before publishing.

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
