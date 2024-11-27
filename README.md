# Bitcoin Price Logger

This project is a **real-time cryptocurrency price logger** built using **Apache Flink**. It fetches live Bitcoin price data (in USD, GBP, and EUR) from the [CoinDesk API](https://api.coindesk.com/v1/bpi/currentprice.json) and logs the prices. **Now, it also supports creating pipelines for individual cryptocurrencies.**

## Features

- Real-time data streaming using Apache Flink.
- Fetches Bitcoin prices from the CoinDesk API.
- **Supports creating pipelines for individual cryptocurrencies.**
- Demonstrates the use of custom source functions and logging in Flink.

## Prerequisites

1. **Java**: JDK 8 or above.
2. **Apache Flink**: Installed and configured. [Download Flink](https://flink.apache.org/downloads.html).
3. **Maven**: For building the project.

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/shreyashag/crypto-flink-pipeline.git
cd crypto-flink-pipeline.git
```

### 2. Build the Project

Use Maven to build the project:

```bash
mvn clean package
```

### 3. Run the Application

Deploy and run the Flink job:

```bash
/path/to/flink/bin/flink run target/cryptoPipeline-1.0-SNAPSHOT.jar
```

### 4. Output

The application logs cryptocurrency prices in the following format:

```
2024-11-27 20:08:46,706 INFO  com.example.cryptoPipeline.CoinGeckoPriceSource              [] - Fetched prices: {bitcoin=CryptoPrice{coinId='bitcoin', price=94638.0, marketCap=1.8717394791857014E12, volume=8.524099652647296E10, change=1.915994061868203}}
2024-11-27 20:08:46,706 INFO  com.example.cryptoPipeline.CoinGeckoPriceSource              [] - Fetched prices: {ethereum=CryptoPrice{coinId='ethereum', price=3488.05, marketCap=4.19972829695645E11, volume=4.146194274541337E10, change=4.92780046511994}}
```

![Bitcoin Price Logger Screenshot](images/screenshot-readme.png)


---

## Project Structure

```
src/main/java/com/example/cryptoPipeline/
    |-- CryptoPriceLogger.java    # Main application entry point
    |-- CryptoPrice.java          # POJO representing cryptocurrency price data
    |-- CoinGeckoPriceSource.java  # Custom source for fetching cryptocurrency prices
    |-- PriceLogSink.java          # Sink for logging cryptocurrency prices
src/main/resources/
    |-- log4j2.properties          # Logging configuration
pom.xml                           # Maven project file
README.md                         # Project documentation
.gitignore                        # Git ignore file
```

## Highlights

- **Flink Skills**:
  - Custom source functions.
  - Integration with external APIs.
  - Real-time data streaming and processing.
- **Logging**:
  - Configured with Log4j 2 for structured output.

---

## Future Enhancements

- Add support for more cryptocurrencies.
- Extend the application to write data to a database or a real-time dashboard.