FROM pgvector/pgvector:pg16

# Set environment variables
ENV POSTGRES_DB=${POSTGRES_DB:-postgres}
ENV POSTGRES_USER=${POSTGRES_USER:-postgres}
ENV POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-postgres}

# The pgvector image already includes the pgvector extension
# Create initialization script to enable pgvector by default
RUN mkdir -p /docker-entrypoint-initdb.d

RUN cat > /docker-entrypoint-initdb.d/init-pgvector.sql << 'EOF'
CREATE EXTENSION IF NOT EXISTS vector;
EOF

# Expose PostgreSQL port
EXPOSE 5432

# Health check
HEALTHCHECK --interval=10s --timeout=5s --start-period=10s --retries=3 \
    CMD pg_isready -U $POSTGRES_USER -d $POSTGRES_DB

CMD ["postgres"]
