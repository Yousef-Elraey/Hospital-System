CREATE TABLE patient(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    phone BIGINT NOT NULL,
    date_of_birth DATE Not NULL,
    created_by varchar(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar(50) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP


);

CREATE TABLE doctor(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    specialty VARCHAR(250) NOT NULL,
    contact_number BIGINT NOT NULL,
    created_by varchar(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar(50) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );


    CREATE TABLE appointment(
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       timing TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       created_by varchar(50) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_by varchar(50) NOT NULL,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       patient_id BIGINT,
       doctor_id BIGINT,
       CONSTRAINT FK_patient_appointment
        FOREIGN KEY (patient_id) REFERENCES patient(id),
       CONSTRAINT FK_doctor_appointment
        FOREIGN KEY (doctor_id) REFERENCES doctor(id)

    );

    CREATE TABLE medical_record (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        diagnose VARCHAR(250) NOT NULL,
        treatment VARCHAR(250) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        created_by VARCHAR(50) NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        updated_by VARCHAR(50) NOT NULL,
        doctor_id BIGINT,
        patient_id BIGINT,

     CONSTRAINT FK_doctor_medical_record
         FOREIGN KEY(doctor_id) REFERENCES doctor(id),
     CONSTRAINT FK_patient_medical_record
      FOREIGN KEY(patient_id) REFERENCES patient(id)
    );
