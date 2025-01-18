# Options Pricing Simulator

This project implements a Monte Carlo simulation for pricing various financial options, including European, Barrier, and Asian options. It also calculates confidence intervals for the estimated option prices and generates charts for visualization.

## Features

- **Option Types Supported**:
    - **European Call and Put**
    - **Barrier Options**: Knock-In and Knock-Out (Call and Put)
    - **Asian Options**: Arithmetic Averaging (Call and Put)
- **Confidence Intervals**: Provides upper and lower bounds for option prices.
- **Chart Generation**:
    - Visualizes option prices and confidence intervals using bar charts.
- **Monte Carlo Simulation**:
    - Simulates option prices using a large number of paths for accurate estimates.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Version 11 or higher.
- **Gradle**: Version 7 or higher.
- **IDE**: IntelliJ IDEA or any Java-supported IDE.

### Dependencies
The project uses the following libraries:
- **JFreeChart**: For chart generation.
- **JUnit**: For testing.

These dependencies are managed via Gradle.

---

## Installation and Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/options-pricing.git
   cd options-pricing
