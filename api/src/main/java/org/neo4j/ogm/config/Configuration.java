/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 *  conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.neo4j.ogm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * A generic configuration class that can be set up programmatically
 * or via a properties file.
 *
 * @author Vince Bickers
 * @author Mark Angrish
 */
public class Configuration {

	private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

	private String uri;
	private int connectionPoolSize;
	private String encryptionLevel;
	private String trustStrategy;
	private String trustCertFile;
	private AutoIndexMode autoIndex;
	private String generatedIndexesOutputDir;
	private String generatedIndexesOutputFilename;
	private String neo4jHaPropertiesFile;
	private String driverName;
	private Credentials credentials;
    private Integer connectionLivenessCheckTimeout;
    private Boolean verifyConnection;


    Configuration(Builder builder) {
		this.uri = builder.uri;
		this.connectionPoolSize = builder.connectionPoolSize != null ? builder.connectionPoolSize : 50;
		this.encryptionLevel = builder.encryptionLevel;
		this.trustStrategy = builder.trustStrategy;
		this.trustCertFile = builder.trustCertFile;
		this.connectionLivenessCheckTimeout = builder.connectionLivenessCheckTimeout;
        this.verifyConnection = builder.verifyConnection != null ? builder.verifyConnection : false;
        this.autoIndex = builder.autoIndex != null ? AutoIndexMode.fromString(builder.autoIndex) : AutoIndexMode.NONE;
		this.generatedIndexesOutputDir = builder.generatedIndexesOutputDir != null ? builder.generatedIndexesOutputDir : ".";
		this.generatedIndexesOutputFilename = builder.generatedIndexesOutputFilename != null ? builder.generatedIndexesOutputFilename : "generated_indexes.cql";
		this.neo4jHaPropertiesFile = builder.neo4jHaPropertiesFile;

		if (this.uri != null) {
			java.net.URI uri = null;
			try {
				uri = new URI(this.uri);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Could not configure supplied URI in Configuration", e);
			}
			String userInfo = uri.getUserInfo();
			if (userInfo != null) {
				String[] userPass = userInfo.split(":");
				credentials = new UsernamePasswordCredentials(userPass[0], userPass[1]);
				this.uri = uri.toString().replace(uri.getUserInfo() + "@", "");
			}
			if (getDriverClassName() == null) {
				determineDefaultDriverName(uri.getScheme());
			}
		} else {
			determineDefaultDriverName("file");
		}
		assert this.driverName != null;

		if (builder.username != null && builder.password != null) {
			if (this.credentials != null) {
				LOGGER.warn("Overriding credentials supplied in URI with supplied username and password.");
			}
			credentials = new UsernamePasswordCredentials(builder.username, builder.password);
		}
	}

	public AutoIndexMode getAutoIndex() {
		return autoIndex;
	}

	public String getDumpDir() {
		return generatedIndexesOutputDir;
	}

	public String getDumpFilename() {
		return generatedIndexesOutputFilename;
	}

	public String getURI() {
		return uri;
	}

	public String getDriverClassName() {
		return driverName;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public String getEncryptionLevel() {
		return encryptionLevel;
	}

	public String getTrustStrategy() {
		return trustStrategy;
	}

	public String getTrustCertFile() {
		return trustCertFile;
	}

    public Integer getConnectionLivenessCheckTimeout() {
        return connectionLivenessCheckTimeout;
    }

    public Boolean getVerifyConnection() {
        return verifyConnection;
    }

    public String getNeo4jHaPropertiesFile() {
        return neo4jHaPropertiesFile;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	private void determineDefaultDriverName(String scheme) {

		if (scheme == null) {
			throw new RuntimeException("A URI Scheme must be one of http/https, bolt or file.");
		}

		switch (scheme) {
			case "http":
			case "https":
				this.driverName = "org.neo4j.ogm.drivers.http.driver.HttpDriver";
				break;
			case "bolt":
				this.driverName = "org.neo4j.ogm.drivers.bolt.driver.BoltDriver";
				break;
			default:
				this.driverName = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver";
				break;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Configuration that = (Configuration) o;

		if (connectionPoolSize != that.connectionPoolSize) return false;
		if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;
		if (encryptionLevel != null ? !encryptionLevel.equals(that.encryptionLevel) : that.encryptionLevel != null)
			return false;
		if (trustStrategy != null ? !trustStrategy.equals(that.trustStrategy) : that.trustStrategy != null)
			return false;
		if (trustCertFile != null ? !trustCertFile.equals(that.trustCertFile) : that.trustCertFile != null)
			return false;
		if (autoIndex != that.autoIndex) return false;
		if (generatedIndexesOutputDir != null ? !generatedIndexesOutputDir.equals(that.generatedIndexesOutputDir) : that.generatedIndexesOutputDir != null)
			return false;
		if (generatedIndexesOutputFilename != null ? !generatedIndexesOutputFilename.equals(that.generatedIndexesOutputFilename) : that.generatedIndexesOutputFilename != null)
			return false;
		if (neo4jHaPropertiesFile != null ? !neo4jHaPropertiesFile.equals(that.neo4jHaPropertiesFile) : that.neo4jHaPropertiesFile != null)
			return false;
		if (!driverName.equals(that.driverName)) return false;
		return credentials != null ? credentials.equals(that.credentials) : that.credentials == null;
	}

	@Override
	public int hashCode() {
		int result = uri != null ? uri.hashCode() : 0;
		result = 31 * result + connectionPoolSize;
		result = 31 * result + (encryptionLevel != null ? encryptionLevel.hashCode() : 0);
		result = 31 * result + (trustStrategy != null ? trustStrategy.hashCode() : 0);
		result = 31 * result + (trustCertFile != null ? trustCertFile.hashCode() : 0);
		result = 31 * result + (autoIndex != null ? autoIndex.hashCode() : 0);
		result = 31 * result + (generatedIndexesOutputDir != null ? generatedIndexesOutputDir.hashCode() : 0);
		result = 31 * result + (generatedIndexesOutputFilename != null ? generatedIndexesOutputFilename.hashCode() : 0);
		result = 31 * result + (neo4jHaPropertiesFile != null ? neo4jHaPropertiesFile.hashCode() : 0);
		result = 31 * result + driverName.hashCode();
		result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
		return result;
	}

	public static class Builder {

		public static Builder copy(Builder builder) {
			return new Builder()
					.uri(builder.uri)
					.connectionPoolSize(builder.connectionPoolSize)
					.encryptionLevel(builder.encryptionLevel)
					.trustStrategy(builder.trustStrategy)
					.trustCertFile(builder.trustCertFile)
					.connectionLivenessCheckTimeout(builder.connectionLivenessCheckTimeout)
                    .verifyConnection(builder.verifyConnection)
                    .autoIndex(builder.autoIndex)
					.generatedIndexesOutputDir(builder.generatedIndexesOutputDir)
					.generatedIndexesOutputFilename(builder.generatedIndexesOutputFilename)
					.neo4jHaPropertiesFile(builder.neo4jHaPropertiesFile)
					.credentials(builder.username, builder.password);
		}

		private static final String URI = "URI";
		private static final String CONNECTION_POOL_SIZE = "connection.pool.size";
		private static final String ENCRYPTION_LEVEL = "encryption.level";
		private static final String TRUST_STRATEGY = "trust.strategy";
		private static final String TRUST_CERT_FILE = "trust.certificate.file";
		private static final String CONNECTION_LIVENESS_CHECK_TIMEOUT = "connection.liveness.check.timeout";
        private static final String VERIFY_CONNECTION = "verify.connection";
        private static final String AUTO_INDEX = "indexes.auto";
		private static final String GENERATED_INDEXES_OUTPUT_DIR = "indexes.auto.dump.dir";
		private static final String GENERATED_INDEXES_OUTPUT_FILENAME = "indexes.auto.dump.filename";
		private static final String NEO4J_HA_PROPERTIES_FILE = "neo4j.ha.properties.file";

		private String uri;
		private Integer connectionPoolSize;
		private String encryptionLevel;
		private String trustStrategy;
		private String trustCertFile;
        private Integer connectionLivenessCheckTimeout;
        private Boolean verifyConnection;
        private String autoIndex;
		private String generatedIndexesOutputDir;
		private String generatedIndexesOutputFilename;
		private String neo4jHaPropertiesFile;
		private String username;
		private String password;

		public Builder() {
		}

		public Builder(ConfigurationSource configurationSource) {
			for (Map.Entry<Object, Object> entry : configurationSource.properties().entrySet()) {
				switch (entry.getKey().toString()) {
					case URI:
						this.uri = (String) entry.getValue();
						break;
					case CONNECTION_POOL_SIZE:
						this.connectionPoolSize = Integer.parseInt((String) entry.getValue());
						break;
					case ENCRYPTION_LEVEL:
						this.encryptionLevel = (String) entry.getValue();
						break;
					case TRUST_STRATEGY:
						this.trustStrategy = (String) entry.getValue();
						break;
					case TRUST_CERT_FILE:
						this.trustCertFile = (String) entry.getValue();
						break;
					case CONNECTION_LIVENESS_CHECK_TIMEOUT:
						this.connectionLivenessCheckTimeout = Integer.valueOf((String) entry.getValue());
						break;
                    case VERIFY_CONNECTION:
                        this.verifyConnection = Boolean.valueOf((String) entry.getValue());
                        break;
                    case AUTO_INDEX:
						this.autoIndex = (String) entry.getValue();
						break;
					case GENERATED_INDEXES_OUTPUT_DIR:
						this.generatedIndexesOutputDir = (String) entry.getValue();
						break;
					case GENERATED_INDEXES_OUTPUT_FILENAME:
						this.generatedIndexesOutputFilename = (String) entry.getValue();
						break;
					case NEO4J_HA_PROPERTIES_FILE:
						this.neo4jHaPropertiesFile = (String) entry.getValue();
						break;
					default:
						LOGGER.warn("Could not process property with key: {}", entry.getKey());
				}
			}
		}

		public Builder uri(String uri) {
			this.uri = uri;
			return this;
		}

		public Builder connectionPoolSize(Integer connectionPoolSize) {
			this.connectionPoolSize = connectionPoolSize;
			return this;
		}

		public Builder encryptionLevel(String encryptionLevel) {
			this.encryptionLevel = encryptionLevel;
			return this;
		}

		public Builder trustStrategy(String trustStrategy) {
			this.trustStrategy = trustStrategy;
			return this;
		}

		public Builder trustCertFile(String trustCertFile) {
			this.trustCertFile = trustCertFile;
			return this;
		}

		public Builder connectionLivenessCheckTimeout(Integer connectionLivenessCheckTimeout) {
			this.connectionLivenessCheckTimeout = connectionLivenessCheckTimeout;
			return this;
		}

        /**
         * Whether OGM should verify connection to the database at creation of the Driver
         * <p>
         * Useful for "fail-fast" type of configuration where the database is expected to be running during application
         * start up and the connection to the database is expected to be very stable.
         * <p>
         * If the connection can't be verified {@link org.neo4j.ogm.exception.ConnectionException} will be thrown during
         * creation of SessionFactory.
         * <p>
         * If set to false the driver will be created when first Session is requested from SessionFactory
         *
         * @param verifyConnection if the connection to the database should be verified, default is false
         */
        public Builder verifyConnection(Boolean verifyConnection) {
            this.verifyConnection = verifyConnection;
            return this;
        }

		public Builder autoIndex(String autoIndex) {
			this.autoIndex = autoIndex;
			return this;
		}

		public Builder generatedIndexesOutputDir(String generatedIndexesOutputDir) {
			this.generatedIndexesOutputDir = generatedIndexesOutputDir;
			return this;
		}

		public Builder generatedIndexesOutputFilename(String generatedIndexesOutputFilename) {
			this.generatedIndexesOutputFilename = generatedIndexesOutputFilename;
			return this;
		}

		public Builder neo4jHaPropertiesFile(String neo4jHaPropertiesFile) {
			this.neo4jHaPropertiesFile = neo4jHaPropertiesFile;
			return this;
		}

		public Configuration build() {
			return new Configuration(this);
		}

		public Builder credentials(String username, String password) {
			this.username = username;
			this.password = password;
			return this;
		}
	}
}
