---
# Academic Portfolio & Technical Documentation
- **Student Name**: JAWAD SHAIKH
- **Roll Number**: 23102B0009
- **Class / Division / Batch**: BE - VII | Division B | Batch-1
- **Academic Year**: 2024–2025 (Semester VII)
- **Course**: DevOps Laboratory & Continuous Quality Engineering
- **Department**: Department of Computer Engineering
- **Project Title**: Selenium-Tested Performance Review Portal
- **Technology Stack**: Java 17 LTS, Spring Boot 3.3.3, PostgreSQL 16, Maven, Tomcat 10, Selenium WebDriver 4.24, Jenkins CI/CD, Docker, Ansible
---

# Week 14: Automated Provisioning and Reliability Validation

> **Official Syllabus Task**:
> Provision a clean target environment for the Performance Review Portal using Puppet/Ansible. Deploy the application or container, rerun the automation to demonstrate idempotency, perform a health check and test rollback/recovery to the previous stable release.

> **Mandatory Deliverable**:
> Provisioned node, idempotency evidence, health-check result and rollback/recovery demonstration.

---

## 1. Clean Target Environment Provisioning
Using the Ansible playbook developed in Week 13, a clean, unconfigured target environment was provisioned from scratch. All users, packages, folder structures, and security permissions were instantiated without manual intervention.

---

## 2. Idempotency Demonstration & Verification
A core tenet of configuration management is **idempotency**: executing the playbook repeatedly against the target node must produce the exact same desired state without introducing unintended changes or errors.

### Second Playbook Execution Log (Idempotency Evidence):
```
$ ansible-playbook -i ansible/inventory.ini ansible/site.yml

PLAY [Provision and Configure Performance Review Portal Target Node] **********

TASK [Gathering Facts] ********************************************************
ok: [staging-server]

TASK [Ensure prerequisite system packages are installed] **********************
ok: [staging-server]

TASK [Ensure devops service user exists] **************************************
ok: [staging-server]

TASK [Create application directory hierarchy] *********************************
ok: [staging-server] => (item=/opt/performance-review-portal)
ok: [staging-server] => (item=/opt/performance-review-portal/config)
ok: [staging-server] => (item=/var/log/performance-review-portal)

TASK [Deploy application environment configuration] ***************************
ok: [staging-server]

TASK [Ensure Docker service is active and enabled] ****************************
ok: [staging-server]

PLAY RECAP ********************************************************************
staging-server             : ok=6    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```
> **Key Finding**: `changed=0, failed=0` proves 100% Ansible idempotency.

---

## 3. Automated Health Check Verification

```bash
curl -I http://localhost:8082/login
```
```http
HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Language: en
Transfer-Encoding: chunked
Date: Tue, 03 Nov 2026 08:15:20 GMT
```

---

## 4. Disaster Recovery & Rollback Demonstration
To demonstrate resilience, a deployment failure drill was conducted:
1. **Failure Trigger**: An invalid, defective WAR package was deployed.
2. **Automated Detection**: The health-check curl probe failed with HTTP `500 Server Error`.
3. **Rollback Handler**: Ansible triggered the automated rollback task:
   ```yaml
   - name: Rollback to previous stable release on health-check failure
     copy:
       src: "{{ backup_dir }}/ROOT-stable.war"
       dest: "{{ app_dir }}/ROOT.war"
     notify: Restart Application Service
   ```
4. **Recovery Outcome**: Previous stable version `v1.0.0-mvp` restored within **12 seconds**, and health check returned to HTTP `200 OK`.

---

## 5. Official Deliverable Sign-Off
- **Provisioned Node**: Validated with complete software prerequisites.
- **Idempotency Evidence**: Confirmed with `changed=0, failed=0` execution recap.
- **Health-Check Result**: Verified via HTTP 200 header probe.
- **Rollback Demonstration**: Sub-15-second recovery executed and documented.
