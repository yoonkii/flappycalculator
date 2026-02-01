# Google Play Store Publishing Guide

## Prerequisites

1. **Google Play Developer Account** ($25 one-time fee)
   - Sign up at: https://play.google.com/console/signup

2. **App Signing Keystore** (CRITICAL - never lose this!)

---

## Step 1: Create Your Keystore

Run this command in Terminal (replace values in CAPS):

```bash
keytool -genkey -v -keystore keystore/release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias flappycalculator \
  -storepass YOUR_SECURE_PASSWORD \
  -keypass YOUR_SECURE_PASSWORD \
  -dname "CN=YOUR_NAME, O=YOUR_ORGANIZATION, L=YOUR_CITY, ST=YOUR_STATE, C=US"
```

Example:
```bash
keytool -genkey -v -keystore keystore/release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias flappycalculator \
  -storepass MySecurePass123! \
  -keypass MySecurePass123! \
  -dname "CN=Yoonki Hong, O=FlappyCalculator, L=San Francisco, ST=CA, C=US"
```

**IMPORTANT**:
- Store this password securely (password manager recommended)
- Back up `release-key.jks` to a safe location
- You CANNOT update your app if you lose the keystore

---

## Step 2: Configure keystore.properties

Create `keystore/keystore.properties`:

```properties
storeFile=../keystore/release-key.jks
storePassword=YOUR_SECURE_PASSWORD
keyAlias=flappycalculator
keyPassword=YOUR_SECURE_PASSWORD
```

---

## Step 3: Build Release App Bundle

```bash
./gradlew bundleRelease
```

The signed AAB will be at:
`app/build/outputs/bundle/release/app-release.aab`

---

## Step 4: Google Play Console Setup

### 4.1 Create App
1. Go to Google Play Console
2. Click "Create app"
3. Fill in:
   - App name: **Flappy Calculator**
   - Default language: English (US)
   - App or game: **Game**
   - Free or paid: **Free** (or Paid if you want)

### 4.2 Store Listing

**Required Assets:**

| Asset | Specification |
|-------|---------------|
| App icon | 512x512 PNG (32-bit, no alpha) |
| Feature graphic | 1024x500 PNG or JPG |
| Screenshots (phone) | Min 2, 320-3840px, 16:9 or 9:16 |
| Screenshots (tablet) | Optional but recommended |

**Required Text:**

| Field | Character Limit |
|-------|----------------|
| Short description | 80 characters |
| Full description | 4000 characters |

**Suggested Content:**

**Short description:**
```
Flappy Bird meets math! Solve equations to make your business analyst fly!
```

**Full description:**
```
Flappy Calculator combines the addictive gameplay of Flappy Bird with mental arithmetic challenges!

HOW TO PLAY:
- Math problems appear on screen
- Enter the correct answer to make your character flap
- Navigate through stock chart obstacles
- Wrong answers won't kill you - just try again!
- Score points by passing obstacles

FEATURES:
- Wall Street themed graphics
- Progressive difficulty (addition to division)
- Local high score tracking
- Satisfying sound effects and haptic feedback
- No ads, no in-app purchases

Challenge your brain while having fun! How high can you score?
```

### 4.3 Content Rating

1. Go to "Content rating" section
2. Complete the IARC questionnaire
3. For this game, expected rating: **Everyone (E)**

Questionnaire tips:
- Violence: None
- Sexual content: None
- Profanity: None
- Controlled substances: None
- User interaction: None (no multiplayer/chat)
- Shares location: No
- Shares personal info: No

### 4.4 Privacy Policy

Even for simple games, Google requires a privacy policy URL.

**Option 1**: Create a simple privacy policy page on GitHub Pages

Create `docs/privacy-policy.html` or host at:
`https://yourusername.github.io/flappycalculator/privacy`

**Sample Privacy Policy:**
```
Privacy Policy for Flappy Calculator

Last updated: [DATE]

Flappy Calculator does not collect, store, or share any personal data.

The app stores your high score locally on your device only.
This data is never transmitted to any server.

The app does not:
- Collect personal information
- Track your location
- Access your contacts
- Use analytics or advertising SDKs
- Connect to the internet

Contact: [YOUR_EMAIL]
```

### 4.5 App Category & Tags

- **Category**: Games > Casual
- **Tags**: puzzle, arcade, math, education, brain training

### 4.6 Target Audience

- Age group: All ages (suitable for children 5+)
- Not designed for children (unless you want additional compliance requirements)

---

## Step 5: Release Management

### 5.1 App Signing by Google Play (Recommended)

1. Go to "App signing" under Release
2. Choose "Let Google manage and protect your app signing key"
3. Upload your AAB file

This provides additional security - Google signs the app for distribution.

### 5.2 Create Release

1. Go to Production > Create new release
2. Upload `app-release.aab`
3. Add release notes:
   ```
   Initial release of Flappy Calculator!
   - Flappy Bird gameplay with math challenges
   - Wall Street themed graphics
   - Progressive difficulty from addition to division
   - Local high score tracking
   ```
4. Review and roll out

---

## Step 6: Review Checklist

Before submitting, verify:

- [ ] App icon uploaded (512x512)
- [ ] Feature graphic uploaded (1024x500)
- [ ] At least 2 phone screenshots
- [ ] Short description filled
- [ ] Full description filled
- [ ] Content rating completed
- [ ] Privacy policy URL set
- [ ] App category selected
- [ ] Target audience configured
- [ ] AAB uploaded
- [ ] Release notes added

---

## Timeline

- **Review time**: Typically 1-3 days for new apps
- **First release**: May take up to 7 days
- **Updates**: Usually reviewed within 24-48 hours

---

## Creating Screenshots

To capture screenshots on an emulator:
1. Run the app on emulator
2. Use the camera button in emulator sidebar
3. Screenshots save to Desktop

Recommended screenshots:
1. Title screen with high score
2. Gameplay showing math problem
3. Gameplay showing bird passing obstacles
4. Game over screen

---

## Troubleshooting

**"App not compatible with any devices"**
- Check minSdk in build.gradle.kts (currently 26)
- Ensure no hardware requirements that limit devices

**"App rejected for policy violation"**
- Common issues: misleading description, inappropriate content
- Review rejection reason and update accordingly

**Build fails with signing error**
- Verify keystore.properties paths are correct
- Ensure keystore file exists at specified location
