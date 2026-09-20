# Language Flashcards

A console-based flashcards application for learning vocabulary across English, Polish, and German - built to demonstrate Dependency Injection, the Dependency Injection Principle, and Spring IoC container in a real, working application.

## Overview

Words are stored with translations across three languages. The app supports full CRUD operations, keyword search, sorting, and a self-testing mode - with a standard feature: **independently selected display formatting per language**.

## Features

- **Add, display, search, modify, and remove** words (individually or all at once)
- **Test Yourself mode** - self-quizzing on stored vocabulary
- **Per-language display profiles** - choose how each language's words are rendered, independently:
    - Original
    - Uppercase
    - Lowercase
- **Sorting** by any of the three languages, ascending or descending
- **Duplicate detection** - prevents adding a word that already exists

## Design & Architecture

This project was built to apply core Spring concepts in a practical way:

- **Dependency Injection / IoC Container** - the application's components (storage, display formatting, input handling) are wired together via Spring's IoC container rather than manually instantiated.
- **Dependency Inversion Principle** - display formatting is implemented as an interface with multiple interchangeable implementation (Original / Uppercase / Lowercase), injected at runtime based on user choice rather than hardcoded.
- **Configuration classes and profiles** - used to manage how components are assembled.
## Tech Stack

- Java
- Spring (IoC / DI)
- Gradle

## Getting Started

### Prerequisites 
- JDK 17+ (or whichever version the project targets)
- Gradle (or use the included Gradle wrapper)

### Run
```bash
git clone https://github.com/dmytrozelinskyy/language-flashcards.git
./gradlew run
```

### Usage

On launch, you will see a menu:

```
--- Menu ---
0 - Exit
1 - Add New Word
2 - Display All Words
3 - Test Yourself
4 - Search Word
5 - Modify Word
6 - Remove Word
7 - Remove All Words
```

When displaying words, you can optionally choose a display profile (Original / Uppercase / Lowercase) independently for each language, and optionally sort the results by any language in ascending or descending order.

## Known Limitations / Planned Improvements 

- No automated tests yet
- No persistent storage across sessions (in-memory only)

## License 

MIT
