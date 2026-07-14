# FinTrack

**FinTrack** is a high-performance, offline-first personal finance manager for Android. Built with a focus on privacy and efficiency, it leverages the modern Android stack to provide a seamless tracking experience with native performance and granular data insights.

## Core Features

*   **Granular Transaction Tracking**: Easily log income and expenses with precise date and time metadata.
*   **Intuitive Dashboard**: At-a-glance view of your total balance, recent activities, and current-month cash flow.
*   **Native Financial Analysis**: Custom-engineered charts providing visual breakdowns of spending by category and income-vs-expense trends.
*   **Flexible Organization**: Manage multiple accounts (Cash, Card, Savings) and customizable categories to fit your financial structure.
*   **Material You Personalization**: Support for dynamic color themes on Android 12+ and a professional custom Teal-Navy palette.
*   **Privacy Centric**: 100% offline-first architecture. Your financial data never leaves your device.

## Technical Implementation

FinTrack is built using modern Android development best practices and the latest Jetpack libraries:

*   **UI Architecture**: 100% **Jetpack Compose** with a focus on reactive state management and custom components.
*   **Graphics**: Native financial charts implemented using the **Compose Canvas API** for maximum performance and fluid animations.
*   **Dependency Injection**: Powered by **Hilt** for a clean, testable, and maintainable codebase.
*   **Persistence**: Robust local storage using **Room Database** with multi-table relations and high-performance queries.
*   **Reactive Patterns**: End-to-end data flow using **Kotlin Coroutines and Flow**, ensuring the UI always reflects the source of truth instantly.
*   **Preferences**: Persistent user settings managed via **Jetpack DataStore**.
*   **Architecture**: Follows **Clean Architecture** principles with a clear separation of concerns (Core, Feature, Repository, UI).

## Getting Started

### Prerequisites
*   Android Studio Ladybug | 2024.2.1 or newer
*   JDK 17
*   Min SDK: 24 (Android 7.0)

### Building the Project
1. Clone the repository: `git clone https://github.com/ubaidraye/FinTrack.git`
2. Open the project in Android Studio.
3. Sync project with Gradle files.
4. Build and run on an emulator or physical device.

## License

```text
Copyright 2024 FinTrack Team

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
