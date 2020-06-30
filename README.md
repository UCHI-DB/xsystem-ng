# XSystem-NG

[![Generic badge](https://img.shields.io/badge/gradle-6.3-<COLOR>.svg)](https://shields.io/)  [![Version](https://badge.fury.io/gh/tterb%2FHyde.svg)](https://badge.fury.io/gh/tterb%2FHyde)

**XSystem** is a method to learn and represent syntactic patterns in datasets as data structures called **XStructures**. Once **XSystem** learns a collection of patterns, it can be used to perform several tasks like: **_automatic label assignment_**, where data items are assigned a class by comparing them to a library of known classes (written as **Regexes** or **XStructures**); finding syntactically similar content, where learned **XStructures** are compared to see if they are similar, and **_outlier detection_**, where a learned **XStructure** for a single item is compared to other **XStructures** to check that its structure is different.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
1. Java
2. Gradle

### Installing

#### Downloading source code
Enter the following command in your console to clone the repository.
```bash
git clone https://github.com/UCHI-DB/xsystem-ng.git
```

#### Adding the Wrapper JAR
Enter the following command in your console to add Gradle Wrapper JAR
```bash
gradle wrapper
```

## Running Tests
And now to run the unit tests, run the following command!
```bash
./gradlew test
```

## Authors

* **Raul Castro Fernandez**
* **Ipsita Mohanty**

