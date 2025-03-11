
# A Tool for Selling Beverages, Desserts, and Related Products

A microservice application designed for managing a coffee shop’s assortment, sales, product inventory, automatic ingredient intake from invoices, and calculating various metrics (sales, salaries, inventory balances, etc.). It enables centralized control over financial flows, warehouse status, and provides real-time sales and analytics data.


## Features

- Automatic ingredient and product intake from uploaded invoices
- Calculation of product costs (beverages and related items) based on cost price and desired profit margin.
- Automatic inventory deduction of ingredients when a beverage is sold or discarded
- Perishable product tracking: a “showcase” feature that keeps track of short-lived products, removing expired goods automatically and deducting them from the warehouse
- End-of-shift summary: daily movement of cash and electronic payments, total number of receipts, and information on which employees worked
- Automated salary calculation for employees based on their position, hours worked, and revenue
- Bonus (Loyalty) Program: provides discounts for customers based on their total purchase volume
- Single entry point secured with Keycloak and OAuth 2.0 for authentication and authorization
- Data collection and monitoring (Kafka → ClickHouse → Grafana) for sales and inventory movements
- Metrics, tracing, and logs available via Grafana, Grafana Loki, Grafana Tempo, and Prometheus


## Architecture
The project employs a microservice architecture, comprising five main services:
- Warehouse Service. Manages incoming products, tracks inventory, and handles deductions for ingredient usage
- Sales Service. Processes orders, calculates costs, and generates sales receipts
- Employee Service. Tracks employee roles, work hours, and salary calculations
- Customer Service. Stores customer data, including purchase history and personal information. Manages the bonus (loyalty) program, applying discounts based on the customer’s total spending
- Finance Service. Monitors cash and electronic transactions, assisting with final shift reports

## Microservices Communication
- All services operate asynchronously, exchanging messages via RabbitMQ
- For analytics, data is published to Kafka, stored in ClickHouse, and visualized in Grafana
- Tracing (Grafana Tempo), logging (Grafana Loki), and metrics (Prometheus) also feed into Grafana
## Technologies Used
- Java with Spring Framework ecosystem
- RabbitMQ – asynchronous message bus for inter-service communication
- Redis - for cashing
- Kafka – streaming platform for analytics data
- ClickHouse – high-performance database for analytical queries
- Grafana – visualization of metrics, logs, and traces (in conjunction with Grafana Tempo, Grafana Loki, Prometheus)
- Keycloak + OAuth 2.0 – single entry point authentication and authorization
- Docker / Kubernetes – containerization/orchestration
- Helm – package manager for Kubernetes deployment
## Installation

1. Clone the repository:

```bash
  git clone https://github.com/MarkHouston322/CoffeeApp.git
  cd CoffeeApp
```
2. Deploy the microservices

You can deploy application using Helm

    1. Make sure you have Helm installed locally
    2. Download or locate the helm folder (or equivalent) from this repository
    3. Navigate to the environment directory (e.g., cd environment)
    4. Run the following command to install the application:
```bash
helm install coffee-app ./dev-env/
```
3. Keycloak Configuration
- Access Keycloak via localhost:80 as an admin with default credentials: username admin, password admin
- Create a client within Keycloak
- Create a user and assign them the EMPLOYEE role (or whichever role is relevant to your setup)

    
## License

[MIT](https://choosealicense.com/licenses/mit/)


## Contact
- Author: Denis Basov
- Email: biofiniy@gmail.com
- LinkedIn: www.linkedin.com/in/denis-basov
