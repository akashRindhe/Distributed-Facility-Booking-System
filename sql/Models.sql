CREATE TABLE User (
	id VARCHAR(255) NOT NULL,
	userName VARCHAR(255) NOT NULL,
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
	userId VARCHAR(255) NOT NULL,
	bookingStart TIMESTAMP NOT NULL DEFAULT current_timestamp,
	bookingEnd TIMESTAMP NOT NULL DEFAULT current_timestamp,
	PRIMARY KEY (id),
	FOREIGN KEY (facilityId) REFERENCES Facility (id),
	FOREIGN KEY (userId) REFERENCES User (id)
);


CREATE TABLE CallbackInfo (
	id INT NOT NULL AUTO_INCREMENT,
	address VARCHAR(255) NOT NULL,
	portNumber INT,
	facilityId INT NOT NULL,
	intervalStart TIMESTAMP NOT NULL DEFAULT current_timestamp,
	intervalEnd TIMESTAMP NOT NULL DEFAULT current_timestamp,
	PRIMARY KEY (id),
	FOREIGN KEY (facilityId) REFERENCES Facility (id)
);
