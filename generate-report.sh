#!/bin/bash

# Test Report Generator Script
# This script runs tests and generates beautiful HTML reports

echo "🚀 Starting Test Report Generation..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven first."
    exit 1
fi

# Clean and run tests with coverage
print_status "Running tests with coverage..."
mvn clean test jacoco:report

if [ $? -eq 0 ]; then
    print_success "Tests completed successfully!"
else
    print_error "Tests failed!"
    exit 1
fi

# Generate Allure report
print_status "Generating Allure report..."
mvn allure:report

if [ $? -eq 0 ]; then
    print_success "Allure report generated!"
else
    print_warning "Allure report generation failed, continuing with other reports..."
fi

# Generate Surefire report
print_status "Generating Surefire report..."
mvn surefire-report:report

# Create a comprehensive HTML report
print_status "Creating comprehensive HTML report..."

REPORT_DIR="target/test-reports"
mkdir -p $REPORT_DIR

# Create the main HTML report
cat > $REPORT_DIR/index.html << 'EOF'
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>API Testing Report</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: #333;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .header {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
        .header h1 {
            color: #2c3e50;
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        
        .header p {
            color: #7f8c8d;
            font-size: 1.2em;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 25px;
            text-align: center;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
        }
        
        .stat-number {
            font-size: 2.5em;
            font-weight: bold;
            margin-bottom: 10px;
        }
        
        .stat-label {
            color: #7f8c8d;
            font-size: 1.1em;
        }
        
        .success { color: #27ae60; }
        .warning { color: #f39c12; }
        .danger { color: #e74c3c; }
        .info { color: #3498db; }
        
        .reports-section {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        
        .reports-section h2 {
            color: #2c3e50;
            margin-bottom: 20px;
            font-size: 1.8em;
        }
        
        .report-links {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 15px;
        }
        
        .report-link {
            display: block;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        
        .report-link:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
        }
        
        .report-link h3 {
            margin-bottom: 10px;
            font-size: 1.3em;
        }
        
        .report-link p {
            opacity: 0.9;
            font-size: 0.9em;
        }
        
        .footer {
            text-align: center;
            color: rgba(255, 255, 255, 0.8);
            padding: 20px;
        }
        
        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }
            
            .header h1 {
                font-size: 2em;
            }
            
            .stats-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🚀 API Testing Report</h1>
            <p>Comprehensive test results and coverage analysis</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number success" id="totalTests">-</div>
                <div class="stat-label">Total Tests</div>
            </div>
            <div class="stat-card">
                <div class="stat-number success" id="passedTests">-</div>
                <div class="stat-label">Passed</div>
            </div>
            <div class="stat-card">
                <div class="stat-number danger" id="failedTests">-</div>
                <div class="stat-label">Failed</div>
            </div>
            <div class="stat-card">
                <div class="stat-number info" id="coverage">-</div>
                <div class="stat-label">Code Coverage</div>
            </div>
        </div>
        
        <div class="reports-section">
            <h2>📊 Detailed Reports</h2>
            <div class="report-links">
                <a href="../surefire-reports/test-report.html" class="report-link" target="_blank">
                    <h3>📋 Surefire Report</h3>
                    <p>Detailed test execution results with pass/fail status</p>
                </a>
                <a href="../site/jacoco/index.html" class="report-link" target="_blank">
                    <h3>📈 Code Coverage</h3>
                    <p>JaCoCo coverage analysis with line-by-line details</p>
                </a>
                <a href="../allure-report/index.html" class="report-link" target="_blank">
                    <h3>🎨 Allure Report</h3>
                    <p>Beautiful interactive test reports with trends</p>
                </a>
            </div>
        </div>
        
        <div class="footer">
            <p>Generated on <span id="generatedDate"></span></p>
        </div>
    </div>
    
    <script>
        // Set generated date
        document.getElementById('generatedDate').textContent = new Date().toLocaleString();
        
        // Try to read test results from surefire reports
        fetch('../surefire-reports/TEST-com.apitester.api.UserApiTest.xml')
            .then(response => response.text())
            .then(data => {
                const parser = new DOMParser();
                const xmlDoc = parser.parseFromString(data, 'text/xml');
                
                const testsuite = xmlDoc.getElementsByTagName('testsuite')[0];
                if (testsuite) {
                    const total = testsuite.getAttribute('tests');
                    const failures = testsuite.getAttribute('failures');
                    const passed = total - failures;
                    
                    document.getElementById('totalTests').textContent = total;
                    document.getElementById('passedTests').textContent = passed;
                    document.getElementById('failedTests').textContent = failures;
                }
            })
            .catch(error => {
                console.log('Could not load test results:', error);
                document.getElementById('totalTests').textContent = 'N/A';
                document.getElementById('passedTests').textContent = 'N/A';
                document.getElementById('failedTests').textContent = 'N/A';
            });
    </script>
</body>
</html>
EOF

print_success "Comprehensive HTML report created!"

# Open the report in default browser
print_status "Opening report in browser..."
if command -v open &> /dev/null; then
    open $REPORT_DIR/index.html
elif command -v xdg-open &> /dev/null; then
    xdg-open $REPORT_DIR/index.html
elif command -v start &> /dev/null; then
    start $REPORT_DIR/index.html
else
    print_warning "Could not automatically open browser. Please open manually:"
    echo "file://$(pwd)/$REPORT_DIR/index.html"
fi

print_success "Report generation completed!"
echo ""
echo "📊 Available Reports:"
echo "  • Main Report: file://$(pwd)/$REPORT_DIR/index.html"
echo "  • Surefire Report: file://$(pwd)/target/surefire-reports/test-report.html"
echo "  • Code Coverage: file://$(pwd)/target/site/jacoco/index.html"
echo "  • Allure Report: file://$(pwd)/target/allure-report/index.html"
echo ""
