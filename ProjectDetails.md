This is Java Project and It is Huge volume system. 
Stirctly follow the OOPs principles. 
use springBoost 
Create the connect and use the same connection accross the Project. 
Use Docker for deployment and create upto 10 nodes based on data. 


Below Are Details: 
  DataBase: TiDB
  WorkFlow : Camunda 8
  Main Job name : Data Collection
  Meta Data Storage : Redis
  During the Batch Process Temporary Storage : DuckDb 


TiDB Login Details:
 username : Chenthil88@gmail.com
 password : ChenSha@2321O6
 Private Key:  c5c3012a-f0ad-4932-95c9-1f85477fffd4
 public Key : 8354RAE0

---

## Spring Boot application (this folder)

- **Artifact:** `rev-rec-service` (Java 21, Spring Boot 3.4). Build: `mvn -B package` from `RevRecProject/`.
- **Java layout:** Base package `com.revrec.engine` — `config`, `web.api` (REST), `domain` (`metadataservice`, `service` row types + `SchemaTables`), `common` (including `common.metadataservice` TiDB/Redis services for metadata tables, `common.service` for journal/arrangement table services, plus row/mapper types under `common`), `dto`, `mapper`, `batch`, `integration` (`camunda`, `redis`), `validation`, `exception`. Tests mirror under `src/test/java/com/revrec.engine`. Resources: `db/migration`, `static`, `processes`.
- **TiDB schema layer (from `ProjectDetails/db_script.sql`):** Java `*Record` types, `*RecordMapper`, Spring `*Service` classes (TiDB + optional Redis `findByIdCached` via `NoSqlRecordServer`). Regenerate: `python3 RevRecProject/scripts/generate_schema_sources.py` (fixes a few DDL typos in the script preprocessor).
- **Shared TiDB access:** one HikariCP `DataSource` plus primary `NamedParameterJdbcTemplate` and `PlatformTransactionManager` in `com.revrec.engine.config.TiDbDataSourceConfig`.
- **Multi-database query engine:** `com.revrec.engine.query` — `QueryEngineService`, dialects (`MySqlDialect`, `DuckDbDialect`), `ConnectionRouter` (`PRIMARY_OLTP` / `BATCH_TEMP`), `SchemaRegistry`, and table repositories. Reference migration: `RevenueContractGroupDetailsRepository` + service.
- **Batch temp:** DuckDB `DataSource` + `batchDuckNamedJdbcTemplate` in `com.revrec.engine.config.BatchDuckDbConfig` (inject explicitly; not primary).
- **Config:** `src/main/resources/application.yml` — use env vars `TIDB_*`, `REDIS_*`, `DUCKDB_JDBC_URL` (see `.env.example`). Prefer secrets manager over storing passwords in this file.
- **Docker:** `Dockerfile` + `docker-compose.yml`. Default is one app instance on port 8080. For multiple nodes (up to 10), use `docker compose up --scale rev-rec=N`; remove or adjust the `ports` mapping on `rev-rec` to avoid host port clashes, and put a load balancer in front.
- **Camunda 8:** add the official Camunda Spring Boot 3 starter when your cluster version is fixed; commented placeholders live in `application.yml`.
