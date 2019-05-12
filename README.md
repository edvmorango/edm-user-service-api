# edm-user-service-api

## More infos at:
https://github.com/edvmorango/event-driven-messenger

## Stack 
__(ZIO + Http4s + Scanamo)__


## Example message payload (will be replaced by __*OpenAPI*__ soon)

```
POST /user/v1/user
Host: localhost:8080
{
	"name": "José E. Vieira M.",
	"email": "jevmor@gmail.com",
	"birthDate": "1996-06-10"
}
```

```
GET /user/v1/user?email=__*<some_mail>*__
Host: localhost:8080
```
