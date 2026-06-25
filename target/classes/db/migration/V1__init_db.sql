
CREATE TABLE patient(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(6) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    date_of_birth DATE Not NULL,
    created_by varchar(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar(50) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE speciality(
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            name_en VARCHAR(50) NOT NULL,
            name_ar VARCHAR(50) NOT NULL
);

CREATE TABLE doctor(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    contact_number VARCHAR(15) NOT NULL UNIQUE,
    speciality_id BIGINT NOT NULL,
    created_by varchar(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar(50) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_doctor_speciality
    FOREIGN KEY(speciality_id) REFERENCES speciality(id)
);

CREATE TABLE appointment_status (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name_en VARCHAR(15),
        name_ar VARCHAR(15)
);

INSERT INTO appointment_status values(1,'new','جديد'),
                                      (2,'paid','مدفوع'),
                                      (3,'pending','قيد الانتظار'),
                                      (4,'cancelled','ملغي'),
                                      (5,'finished','انتهاء');


CREATE TABLE appointment(
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       timing TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       appointment_type VARCHAR(50) NOT NULL,
       created_by varchar(50) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_by varchar(50) NOT NULL,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       patient_id BIGINT NOT NULL,
       doctor_id BIGINT NOT NULL,
       status_id BIGINT NOT NULL,

       CONSTRAINT FK_patient_appointment
        FOREIGN KEY (patient_id) REFERENCES patient(id),
       CONSTRAINT FK_doctor_appointment
        FOREIGN KEY (doctor_id) REFERENCES doctor(id),
       CONSTRAINT FK_status_appointment
        FOREIGN KEY (status_id) REFERENCES appointment_status(id)
);

CREATE TABLE diagnose(
        id BIGINT AUTO_INCREMENT PRIMARY key,
        name_en VARCHAR(255) NOT NULL,
        name_ar VARCHAR(255) NOT NULL
      );

CREATE TABLE treatment(
        id BIGINT AUTO_INCREMENT PRIMARY key,
        name_en VARCHAR(255) NOT NULL,
        name_ar VARCHAR(255) NOT NULL,
        active_ingredient varchar(255) NOT NULL
      );

CREATE TABLE medical_record (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        diagnose_id BIGINT NOT NULL,
        treatment_id BIGINT NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        created_by VARCHAR(50) NOT NULL,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        updated_by VARCHAR(50) NOT NULL,
        doctor_id BIGINT,
        patient_id BIGINT,

     CONSTRAINT FK_doctor_medical_record
         FOREIGN KEY(doctor_id) REFERENCES doctor(id),
     CONSTRAINT FK_patient_medical_record
      FOREIGN KEY(patient_id) REFERENCES patient(id),
     CONSTRAINT FK_diagnose_medical_record
            FOREIGN KEY(diagnose_id) REFERENCES diagnose(id),
     CONSTRAINT FK_treatment_medical_record
            FOREIGN KEY(treatment_id) REFERENCES treatment(id)
);

CREATE TABLE billing(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    created_by varchar(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by varchar(50) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

     CONSTRAINT FK_billing_patient
      FOREIGN KEY(patient_id) REFERENCES patient(id)
);

CREATE TABLE users(
        id BIGINT AUTO_INCREMENT PRIMARY key,
        user_name varchar(50) NOT NULL,
        password varchar(100) NOT NULL);