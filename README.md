# 🌆 TripMedia  (Media Service - Microservice 6)



---

### 🔗 [TripWise-Architecture 🍀 Overview Repository ](https://github.com/Ochwada/TripWise-Architecture)

Microservices ⬇️ part of **TripWise System**


#### 🖇️ Microservice 0: TripHub - [ Gateway  Service]( )
#### 🖇️ Microservice 1: TripPass - [ Authentication Service](https://github.com/Ochwada/TripWise-Pass)
#### 🖇️ Microservice 2: TripProfile - [ User Profile Service ](https://github.com/reyhanovelek/TripProfile-Service)
#### 🖇️ Microservice 3: TripPlanner - [ Planner Service](https://github.com/Jind19/TripWise_Planner)
#### 🖇️ Microservice 4: TripJournal - [ Journal Service](https://github.com/Ochwada/TripWise-Journal)
#### 🖇️ Microservice 5: TripWeather  - [ Weather Service](https://github.com/OrnellaDelVicario/tripwise_tripweather)
#### 🖇️ Microservice 6: TripMedia - [ Media Service](https://github.com/Ochwada/TripWise-Media)

---

## About
Media upload & processing microservice (practice project) for the TripWise ecosystem. Handles upload init/confirm, 
stores metadata in MongoDB, pushes bytes to S3-compatible object storage, and (optionally) triggers async enrichment 
(EXIF, thumbnails, WebP/AVIF).

> Built to practice Java backend skills with Spring Boot 3, MongoDB, OAuth2, AWS SDK v2, and OpenAPI.

## Features
- Upload lifecycle 
  - POST /media/uploads:init → returns pre-signed URL + storage key 
  - PUT bytes to object storage (S3/MinIO/R2)
  - POST /media/uploads:confirm → persist metadata, checksum, dimensions 
- Metadata store: MongoDB (Spring Data)
- Security (optional): OAuth2 Resource Server (JWT bearer)
- Validation: DTO constraints (@NotBlank, @Positive, etc.)
- Docs: OpenAPI UI via springdoc
- (Planned) Async worker to add EXIF, thumbnails, WebP/AVIF variants

##  Tech Stack
- Java 17, Spring Boot 3.5.5 
- Spring Data MongoDB 
- OAuth2 Resource Server 
- AWS SDK v2 (S3) — works with AWS S3, MinIO, Cloudflare R2 
- springdoc-openapi 
- dotenv-java

## Architecture (high level)

Client → TripMedia → Object Storage (S3/MinIO/R2) → TripJournal API → MongoDB 

![Flowchart - Media Service.jpg](assets/Flowchart%20-%20Media%20Service.jpg)

## Project Structure

```
├── assets/
│   └── Flowchart - Media Service.jpg
├── src/
│   └── main/
│       ├── java/
│       │   └── com/tripwise/tripmedia/
│       │       ├── TripmediaApplication.java
│       │       ├── config/
│       │       │   ├── JournalWebClientConfig.java
│       │       │   ├── MediaProps.java
│       │       │   ├── MongoConfig.java
│       │       │   ├── S3Config.java
│       │       │   └── SecurityConfig.java
│       │       ├── controller/
│       │       │   └── MediaController.java
│       │       ├── dto/
│       │       │   ├── ConfirmUploadRequest.java
│       │       │   ├── InitUploadRequest.java
│       │       │   ├── InitUploadResponse.java
│       │       │   └── MediaResponse.java
│       │       ├── expectation/
│       │       │   └── GlobalExceptionHandler.java
│       │       ├── model/
│       │       │   ├── Media.java
│       │       │   ├── MediaStatus.java
│       │       │   ├── MediaVariant.java
│       │       │   └── VariantType.java
│       │       ├── repository/
│       │       │   └── MediaRepository.java
│       │       └── service/
│       │           ├── MediaService.java
│       │           └── client/
│       │               ├── JournalClient.java
│       │               ├── PresignedPut.java
│       │               ├── S3StorageClient.java
│       │               └── StorageClient.java
│       └── resources/
│           ├── static/
│           ├── templates/
│           ├── application.yml
│           └── application-dev.yml

```
#### Module Overview


| Folder              | Classes / Files                           | Purpose                                                       |
|---------------------|-------------------------------------------|---------------------------------------------------------------|
| **config/**         | `JournalWebClientConfig`                  | Configures WebClient for calling TripJournal API.             |
|                     | `MediaProps`                              | Typed configuration properties (bucket, region, etc.).        |
|                     | `MongoConfig`                             | Sets up MongoDB connection and templates.                     |
|                     | `S3Config`                                | Configures S3 client (AWS SDK v2, MinIO, R2).                 |
|                     | `SecurityConfig`                          | OAuth2 resource server & security rules.                      |
| **controller/**     | `MediaController`                         | REST endpoints: init upload, confirm upload, fetch media.     |
| **dto/**            | `ConfirmUploadRequest`                    | Request model to confirm uploaded file metadata.              |
|                     | `InitUploadRequest`                       | Request model to start an upload (filename, mime, journalId). |
|                     | `InitUploadResponse`                      | Response model with `uploadUrl`, `storageKey`, `mediaId`.     |
|                     | `MediaResponse`                           | Response model for retrieving media info.                     |
| **expectation/**    | `GlobalExceptionHandler`                  | Maps exceptions into structured HTTP responses.               |
| **model/**          | `Media`                                   | MongoDB document storing media metadata.                      |
|                     | `MediaStatus`                             | Enum for status: PENDING, READY, etc.                         |
|                     | `MediaVariant`                            | Representation of thumbnails/variants.                        |
|                     | `VariantType`                             | Enum for variant type (e.g., ORIGINAL, THUMBNAIL, WEBP).      |
| **repository/**     | `MediaRepository`                         | Spring Data MongoDB repository for `Media`.                   |
| **service/**        | `MediaService`                            | Business logic for upload/confirm/retrieve.                   |
| **service/client/** | `JournalClient`                           | REST client to push media links into TripJournal API.         |
|                     | `PresignedPut`                            | Helper for S3 pre-signed PUT URL generation.                  |
|                     | `S3StorageClient`                         | Handles S3/MinIO/R2 object storage operations.                |
|                     | `StorageClient`                           | Generic storage client abstraction.                           |
| **resources/**      | `application.yml` / `application-dev.yml` | Environment configs for dev & prod.                           |
|                     | `static/` / `templates/`                  | Reserved for static assets / template views.                  |

##  Environment Configurations

Environment variables are defined in:

```
tripmedia-service/.env
```
These credentials configure MongoDB, Google OAuth2/OpenID Connect, and storage (Cloudflare R2 / MinIO).

```.dotenv
# ------------------------------------------------------
# TripWise Media Service (Dev)
# ------------------------------------------------------

# Spring profile (loads application-dev.yml)
SPRING_PROFILES_ACTIVE=***

# Application name
SPRING_APPLICATION_NAME=***

# ------------------------------------------------------
# MongoDB Atlas (free M0 cluster)
# ------------------------------------------------------
SPRING_DATA_MONGODB_URI=***

# ------------------------------------------------------
# Google OAuth2 / OpenID Connect
# ------------------------------------------------------
GOOGLE_CLIENT_ID=***
GOOGLE_CLIENT_SECRET=***   # keep this secure!

# ------------------------------------------------------
# Media bucket (Media service config)
# ------------------------------------------------------
MEDIA_BUCKET=***

# Public base URL for serving media (Cloudflare R2 or static mapping)
MEDIA_PUBLIC_BASE_URL=***

# ------------------------------------------------------
# Cloudflare R2 / MinIO (S3-compatible storage)
# ------------------------------------------------------
MEDIA_S3_ENDPOINT=***     # e.g. https://<account>.r2.cloudflarestorage.com
#MEDIA_S3_ENDPOINT=***    # example for local MinIO
MEDIA_S3_REGION=auto
MEDIA_S3_ACCESS_KEY=***
MEDIA_S3_SECRET_KEY=***
MEDIA_S3_PATH_STYLE_ACCESS=true

# ------------------------------------------------------
# TripJournal (external service)
# ------------------------------------------------------
JOURNALS_BASE_URL=***
# e.g. https://journals.your-domain.com

# ------------------------------------------------------
# Useful Dev Endpoints
# ------------------------------------------------------
# Swagger:  http://localhost:8082/swagger-ui.html
# MinIO:    http://localhost:9001

```
> These credentials are used to authenticate users via Google OAuth2/OpenID Connect.



#### Variable Reference

| Variable                  | Description                                | Where to Get It                                      |
|---------------------------|--------------------------------------------|------------------------------------------------------|
| `SPRING_DATA_MONGODB_URI` | MongoDB connection string                  | From MongoDB Atlas (or local Mongo instance)         |
| `GOOGLE_CLIENT_ID`        | OAuth2 client ID for Google login          | Google Cloud Console → APIs & Services → Credentials |
| `GOOGLE_CLIENT_SECRET`    | OAuth2 client secret (authentication)      | Same place as above (⚠️ keep secure!)                |
| `MEDIA_BUCKET`            | S3/R2 bucket name                          | Create in MinIO, Cloudflare R2, or AWS S3            |
| `MEDIA_PUBLIC_BASE_URL`   | Public base URL for serving uploaded files | Cloudflare R2 public URL or local mapping            |
| `MEDIA_S3_*`              | S3/R2/MinIO storage configuration          | From your provider or local MinIO setup              |
| `JOURNALS_BASE_URL`       | URL to TripJournal API                     | Your deployed service or local dev endpoint          |


Service will be available at:
```yaml
# Localhost
http://localhost:9096/tripmedia

# Dockerized
https://tripwise:9096/tripmedia
```