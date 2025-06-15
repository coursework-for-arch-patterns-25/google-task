# Google OAuth2 Spring Boot Application

A minimal Spring Boot application demonstrating Google OAuth2 integration.

## Features

- Welcome page with Google sign-in button
- OAuth2 authentication with Google
- User account page displaying name and surname
- Logout functionality

## Prerequisites

- Java 17 or later
- Maven 3.6 or later

## Setup

1. **Create Google OAuth2 Credentials**
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing one
   - Enable Google+ API
   - Create OAuth 2.0 credentials
   - Set authorized JavaScript origin to: `http://localhost:8080`
   - Set authorized redirect URI to: `http://localhost:8080/login/oauth2/code/google`

2. **Set Environment Variables**
   ```bash
   export GOOGLE_CLIENT_ID=your_google_client_id_here
   export GOOGLE_CLIENT_SECRET=your_google_client_secret_here
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**
   - Open your browser and go to `http://localhost:8080`
   - Click "Sign in with Google"
   - After successful authentication, you'll be redirected to the account page
   - After unsuccessful authentication, you'll be redirected to home page with error message

## Application Endpoints

- `/` - Home page with Google sign-in button
- `/account` - User account page (requires authentication)
- `/logout` - Logout endpoint

## For Product Owners (Non-Technical)

```mermaid
sequenceDiagram
    participant User
    participant App as Our App
    participant Google as Google

    User->>App: 1. Visits homepage
    App->>User: 2. Shows "Sign in with Google" button
    User->>App: 3. Clicks sign in button
    App->>Google: 4. Redirects to Google login
    Google->>User: 5. Shows Google login form
    User->>Google: 6. Enters credentials
    
    alt Login Success
        Google->>App: 7. User approved, sends back to app
        App->>User: 8. Shows account page with name
        User->>App: 9. Can logout
        App->>User: 10. Returns to homepage
    else Login Failed
        Google->>App: 7. Login failed
        App->>User: 8. Shows error on homepage
    end
```

## For Developers (Technical)

```mermaid
sequenceDiagram
    participant Browser
    participant SpringApp as Spring Boot App
    participant GoogleAuth as Google OAuth2 Server
    participant GoogleAPI as Google User Info API

    Browser->>SpringApp: GET /
    SpringApp->>Browser: 200 OK (index.html with login button)
    Browser->>SpringApp: GET /oauth2/authorization/google
    SpringApp->>Browser: 302 Redirect to Google OAuth2
    Browser->>GoogleAuth: GET authorization endpoint
    GoogleAuth->>Browser: Login form
    Browser->>GoogleAuth: POST credentials
    
    alt Authentication Success
        GoogleAuth->>Browser: 302 Redirect with authorization code
        Browser->>SpringApp: GET /login/oauth2/code/google?code=xyz
        SpringApp->>GoogleAuth: POST token exchange (code for access token)
        GoogleAuth->>SpringApp: 200 OK (access token + id token)
        SpringApp->>GoogleAPI: GET user info with access token
        GoogleAPI->>SpringApp: 200 OK (user profile: given_name, family_name)
        SpringApp->>Browser: 302 Redirect to /account
        Browser->>SpringApp: GET /account
        SpringApp->>Browser: 200 OK (account.html with user data)
        Browser->>SpringApp: GET /logout
        SpringApp->>Browser: 302 Redirect to / (session cleared)
        
    else Authentication Failed
        GoogleAuth->>Browser: 302 Redirect with error
        Browser->>SpringApp: GET /login/oauth2/code/google?error=access_denied
        SpringApp->>Browser: 302 Redirect to /?error=true
        Browser->>SpringApp: GET /?error=true
        SpringApp->>Browser: 200 OK (index.html with error message)
    end
```

## Application Screenshots

### Welcome Page
![Welcome Page](images/welcome.png)

### Account Page
![Account Page](images/account.png)

### Error State
![Error Page](images/error.png)