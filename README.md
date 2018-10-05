# Payment gate Spring example
This is an example integration of Spring Boot 2 and some popular Polish payment gates:
Dotpay and Przelewy24.

_**NOTE!** This is **NOT** a production-ready solution! I do not take responsibility
for any unsafe usage! You should consult the code with Dotpay/Przelewy24 technical staff
before using it in production!_ 

## Goals
- Integrate any Polish payment gate with Spring Boot. Most of existing examples are
written in PHP.
- Make boilerplate code for production-ready universal payment integration in future
- Practise some design patterns

##### The point
This example aims at simple paid-subscribtion system for users. Each user can perform
a payment to update his account to paid subscribtion. I planned to create expiring,
monthly subscribtion plan, but none of the payment gates supports recurring
transactions for free in their sandbox environment.

This example aims at user subscribtions, but I try to make it flexible enough
to fit any online shop & basket without much code modification.

## Resources
Technical information about payment gate integration:
- [Dotpay implementation manual (PDF)](https://ssl.dotpay.pl/s2/login/cloudfs1/magellan_media/common_file/dotpay_technical_manual_for_payments_implementation.pdf)
- [Przelewy24 integration manual (PDF)](https://www.przelewy24.pl/eng/storage/app/media/pobierz/Instalacja/przelewy24_specification.pdf)

## Roadmap
- Finish implementing Przelewy24 _(I cannot test it for now, because I do not have sandbox account)_
- Completely separate payment logic from app-specific business logic
- Unify payment interfaces for each gate
- Implement PayU support
- Implement SMS Premium support
- Add built-in support for recurring transactions
- Test security
- Write some unit and integration tests.

## Project structure
See `DotpayController` class code and Dotpay documentation to find out how it works.
There is a request construction code, which is then rendered as a button and shown to user.
Also there's a `URLC` status endpoint, where notifications from Dotpay are sent and processed.

Przelewy24 integration is based partially on a PHP exaple on their [website](https://www.przelewy24.pl/pobierz#instalacja). Work in progress...

#### Configuration
In order to run it on your own, you have to register a sandbox account (for example on Dotpay website),
then do the proper configuration in `application.properties`.