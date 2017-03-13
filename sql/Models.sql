USE DATABASE dist_systems;

-- Instead of id, we use Matriculation number field
CREATE TABLE User (
	id INT NOT NULL AUTO_INCREMENT,
	email VARCHAR(255) NOT NULL,
	PRIMARY KEY (id)
);

 
CREATE TABLE Facility (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE Booking (
	id INT NOT NULL AUTO_INCREMENT,
	facilityId INT NOT NULL,
	userId INT NOT NULL,
	bookingStart TIMESTAMP NOT NULL,
	bookingEnd TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (facilityId) REFERENCES Facility (id),
	FOREIGN KEY (userId) REFERENCES USer (id)
);


CREATE TABLE CallbackInfo (
	id INT NOT NULL AUTO_INCREMENT,
	address VARCHAR(255) NOT NULL,
	portNumber INT,
	facilityId INT NOT NULL,
	intervalStart TIMESTAMP NOT NULL,
	intervalEnd TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (facilityId) REFERENCES Facility (id)
);
