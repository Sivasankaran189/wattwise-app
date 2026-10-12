-- Docker PostgreSQL Initialization Script for Local Development
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Notice: Flyway handles actual tables and migrations automatically.
-- This script ensures database encoding and extensions are ready upon container boot.
SELECT 'WattWise local database initialized' AS status;
