#!/bin/bash

#Get output values, this is a somewhat naive approach since it is a lot of api calls

newman run integration/MoodleTenant.postman_collection.json --timeout 5000