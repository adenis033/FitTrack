# FitTrack

FitTrack is a robust Android fitness tracker application tailored to help users set, monitor, and achieve their fitness goals. Built using Kotlin in Android Studio, the app combines intuitive design with powerful functionality to deliver an exceptional fitness tracking experience. By leveraging Firebase for secure data handling and external APIs for added motivation, FitTrack empowers users to take control of their fitness journey.

Designed for all fitness enthusiasts, whether beginners or seasoned athletes, FitTrack offers real-time activity tracking, goal management, and progress visualization—all in one place. With a focus on usability and performance, the app ensures users stay motivated, consistent, and informed.

## Features

FitTrack includes a variety of features to enhance the fitness journey:

- **User Authentication:** Secure login and registration functionality powered by Firebase.
- **Activity Tracking:** Real-time tracking of:
  - Running
  - Cycling
  - Weight Lifting
  - HIIT (High-Intensity Interval Training)
  - Yoga
- **Real-Time Metrics:** Track calories burned, distance covered, and time spent on each activity.
- **Goal Management:** Set personal fitness goals and monitor your progress.
- **Recent Activities:** Review a detailed log of your past workouts.
- **Progress Dashboard:** Visualize your fitness journey with aggregated performance data.
- **Notifications:** Get timely reminders and motivational alerts to stay on track.
- **Integration with External APIs:** Provide users with regularly updated motivational quotes to keep them inspired during their fitness journey.

---

## How the App Works

### Login/Registration
- Start by creating an account or logging into an existing one.
- User credentials are securely handled using **Firebase Authentication**.

### Home Page
After logging in, users land on the home page, which offers four main options:
1. **Start a New Activity:** Select from running, cycling, weight lifting, HIIT, or yoga.
2. **Recent Activity:** Access a log of your completed workouts.
3. **Goals:** Check your personal goals to stay motivated and track achievements.
4. **My Progress:** View your performance trends in the "My Progress" section.

### Activity Tracking
- Select an activity to begin real-time tracking.
- The app measures key metrics such as:
  - **Time Spent**
  - **Calories Burned**
  - **Distance Covered** (for activities like running or cycling).

### Goal Management
- Set personalized fitness goals, like calories to burn or hours to exercise.
- Track your progress and adjust your goals as needed.

### Progress Dashboard
- Get an overview of your fitness journey with aggregated data and performance summaries.

### Notifications
- Stay engaged with:
  - Congratulatory messages when you achieve milestones.

---

## Android Components Used

FitTrack demonstrates the implementation of key Android development components:

- **Activities:** Manage user navigation across multiple screens (e.g., login, activity tracking, and progress dashboard).
- **Intents:** Enable seamless communication between app components.
- **Foreground Services:** Support continuous tracking of activities, even when the app is minimized (for example, displaying a timer to show how much time you’ve spent working out while the app runs in the background).
- **Background Services:** Handle data syncing and background tasks without interrupting the user (for example, calculating how many calories your body burned while idle or performing no activity).
- **Shared Preferences:** Store user-specific settings, such as recent activity logs or preferences, locally on the device for quick and easy access.
- **Database:** Use **Firebase Realtime Database** for secure storage of activity logs, goals, and user data.
- **Notifications:** Deliver real-time updates, reminders, and motivational alerts to enhance engagement.
- **External APIs:** Provide users with regularly updated motivational quotes to inspire them during their fitness journey.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/adenis033/FitTrack.git

