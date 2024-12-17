# Backend Engineering Case Study

For Postman API endpoints, check the folder Postman please.

Add brief explanation of how you organized your implementation and the choices you made in terms of design while solving problems
Overview:

This repository contains the backend implementation of the user progress management and "Pop The Balloon"
LiveOps event system, as well as a global leaderboard feature. Users can create profiles,
progress through levels, participate in a time-limited cooperative event to earn rewards,
 and view a global leaderboard of top players.

Project Structure:

Controllers: REST endpoints for Users, Pop Balloon Event actions, and Leaderboard retrieval.

DTOs: Data Transfer Objects for requests and responses (e.g., CreateUserRequest, UpdateLevelRequest, InvitePartnerRequest, etc.).

Entities: JPA entities representing database tables (User, PopBalloonEventParticipation, PopBalloonEventInvitation).

Repositories: Spring Data JPA repositories interfacing with MySQL database tables.

Services: Core business logic resides here. Handles validation, event state checks, A/B group assignments, helium accumulation, balloon inflation logic, and reward claiming.

Exception Handling: A global exception handler to return structured error responses.

Key Features and Design Choices
User Progress and A/B Testing:

Users start with level=1, coins=2000, randomly assigned to A or B group.
On completing a level, users gain 100 coins and increment their level.
The random A/B assignment is done once at user creation to support A/B testing.
Pop The Balloon Event Mechanics:

Event Active Time: 08:00 to 22:00 UTC.
Participation requires level≥50 and 2500 coins as an entry fee.
Users in the event collect helium (10 per level completion) which can be used to inflate a shared balloon with a partner.
Balloon targets differ by A/B group (1000 for Group A, 1500 for Group B).
After popping the balloon, both partners can claim a reward (1000 coins for A, 1500 for B).
Invitations flow: suggest partners, invite, accept, reject.
Database is updated transactionally to prevent concurrency issues.
Global Leaderboard:

Returns top 100 players by level.
Indexed queries ensure efficient retrieval under high load.
By default, the current implementation queries MySQL directly. For higher performance under extreme load, consider caching with Redis or in-memory structures.
Performance and Concurrency Considerations:

Indexed columns (e.g., idx_level on users) to speed up leaderboard queries.
Use of @Transactional ensures atomic operations during event participation and partner matching.
Scoped queries (like findTop100ByOrderByLevelDesc()) reduce large result sets.
Potential future improvements might include caching the leaderboard or introducing asynchronous updates.

Testing:

Extensive unit and integration tests cover:
User creation and level updates
Joining the event, inviting, accepting, rejecting
Balloon inflation and reward claiming
Leaderboard retrieval
Tests use H2 in-memory DB for fast execution.
Separate tests exist for scenarios with the event active and inactive.

Running the Project:

Prerequisites: Docker and Docker Compose.

Build and Run:

docker-compose up
This starts the Spring Boot application and a MySQL container.

Database Initialization:

The mysql-db-dump.sql is mounted into the MySQL container and creates the required tables.
Profiles and Configuration:

application.properties for development and application-test.properties for tests (H2 in-memory DB).
For production, ensure to point spring.datasource.url to the actual MySQL instance.

Design Choices Rationale
Spring Boot + JPA: Quickly implement REST endpoints and database integration.
Random A/B Group: Balanced user distribution for A/B testing.
Checked Event Activity Times: Provides business logic to ensure correct event lifecycle.
Clean Code: Services handle logic, Controllers handle I/O, Repositories handle persistence.
Scalability: Indexed queries, potential caching strategies, and transactional boundaries prepare for higher load scenarios.