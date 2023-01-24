require('dotenv').config();
const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20').Strategy;

const User = require("../models/user");
const GOOGLE_CALLBACK_URL = "http://localhost:4000/api/v1/auth/google/callback";

passport.use(new GoogleStrategy({
    clientID: process.env.GOOGLE_CLIENT_ID,
    clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    callbackURL: GOOGLE_CALLBACK_URL,
    passReqToCallback: true
}, async (req, accessToken, refreshToken, profile, cb) => {
    // Create a user with the received profile
    const defaultUser = {
        googleId: profile.id,
        fName: profile.name.givenName,
        lName: profile.name.familyName,
        email: profile.emails[0].value,
        picture: profile.photos[0].value
    };

    // Check if this user already exists in the database
    // If it doesn't, then create it
    const user = await User.findOrCreate({
        where: { googleId: profile.id }, defaults: defaultUser
    }).catch(err => {
        console.log("Error during user signup.", err);
        cb(err, null);
    });
    // Check if the db query was successful
    if (user && user[0]) {
        // If successful, return the user
        return cb(null, user && user[0]);
    }
}));

passport.serializeUser((user, cb) => {
    console.log("Serializing user: ", user);
    cb(null, user);
});

passport.deserializeUser(async (id, cb) => {
    const user = await User.findOne({ where: { id } }).catch((err) => {
        console.log("Error deserializing.", err);
        cb(err, null);
    });
    console.log("Deserialized user", user);
    cb(null, user);
});