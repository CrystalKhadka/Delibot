<!DOCTYPE html>
<html>
<head>
    <title>OTP Verification</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f0f0f0;
        }

        .container {
            background-color: #ffffff;
            border-radius: 8px;
            padding: 20px;
        }

        h2 {
            text-align: center;
            color: #007bff;
        }

        .otp-message {
            color: #333333;
        }

        .otp-code {
            font-weight: bold;
            font-size: 24px;
            color: #007bff;
        }

        .email-footer {
            text-align: center;
            margin-top: 20px;
            color: #666666;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>OTP Verification</h2>
    <p class="otp-message">Dear ${name},</p>
    <p class="otp-message">Your One-Time Password (OTP) for verification is: <span class="otp-code">${otp}</span></p>
    <p class="otp-message">Please use this OTP to complete the verification process. Do not share it with anyone.</p>
    <p class="otp-message">If you didn't request this OTP, please ignore this email.</p>
</div>
<div class="email-footer">
    <p>Thank you,</p>
    <p>Your Team</p>
</div>
</body>
</html>
