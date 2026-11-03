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

# Week 13: Configuration Management Script (Ansible)

> **Official Syllabus Task**:
> Identify all server prerequisites for the Performance Review Portal: packages, users, folders, files, ports and services. Create either a Puppet manifest/modules or an Ansible inventory and YAML playbook to configure the target node.

> **Mandatory Deliverable**:
> Configuration specification, Puppet manifest or Ansible playbook and first execution log.

---

## 1. Target Node Server Prerequisites Specification
To achieve automated provisioning, the target server node must meet the following configuration specifications:
- **System Packages**: `openjdk-17-jdk`, `postgresql-16`, `curl`, `git`, `docker.io`.
- **System Users & Groups**: Dedicated service user `devops` with sudoers privileges.
- **Filesystem Hierarchy**:
  - `/opt/performance-review-portal/` (Application binary directory)
  - `/opt/performance-review-portal/config/` (Environment configurations)
  - `/var/log/performance-review-portal/` (Application logs)
- **Networking & Ports**: Inbound access open on `8081` (Tomcat), `8082` (Docker), and `5432` (PostgreSQL).

---

## 2. Ansible Inventory (`ansible/inventory.ini`)

```ini
[staging]
staging-server ansible_host=127.0.0.1 ansible_connection=local

[production]
prod-server-01 ansible_host=192.168.1.100 ansible_user=devops

[all:vars]
ansible_python_interpreter=/usr/bin/python3
app_port=8082
db_port=5432
```

---

## 3. Ansible Playbook (`ansible/site.yml`)

```yaml
---
- name: Provision and Configure Performance Review Portal Target Node
  hosts: staging
  become: yes
  vars:
    app_dir: /opt/performance-review-portal
    app_version: "v1.0.0-mvp"

  tasks:
    - name: Ensure prerequisite system packages are installed
      apt:
        name:
          - openjdk-17-jdk
          - postgresql-client
          - curl
          - docker.io
        state: present
        update_cache: yes

    - name: Ensure devops service user exists
      user:
        name: devops
        shell: /bin/bash
        groups: docker
        append: yes

    - name: Create application directory hierarchy
      file:
        path: "{{ item }}"
        state: directory
        owner: devops
        group: devops
        mode: '0755'
      loop:
        - "{{ app_dir }}"
        - "{{ app_dir }}/config"
        - /var/log/performance-review-portal

    - name: Deploy application environment configuration
      copy:
        dest: "{{ app_dir }}/config/application-prod.properties"
        content: |
          spring.datasource.url=jdbc:postgresql://localhost:5432/performance_review
          spring.datasource.username=postgres
          spring.datasource.password=Jawadsk@12
          server.port=8082
        owner: devops
        group: devops
        mode: '0600'

    - name: Ensure Docker service is active and enabled
      service:
        name: docker
        state: started
        enabled: yes
```

---

## 4. First Execution Log

```
$ ansible-playbook -i ansible/inventory.ini ansible/site.yml

PLAY [Provision and Configure Performance Review Portal Target Node] **********

TASK [Gathering Facts] ********************************************************
ok: [staging-server]

TASK [Ensure prerequisite system packages are installed] **********************
changed: [staging-server]

TASK [Ensure devops service user exists] **************************************
changed: [staging-server]

TASK [Create application directory hierarchy] *********************************
changed: [staging-server] => (item=/opt/performance-review-portal)
changed: [staging-server] => (item=/opt/performance-review-portal/config)
changed: [staging-server] => (item=/var/log/performance-review-portal)

TASK [Deploy application environment configuration] ***************************
changed: [staging-server]

TASK [Ensure Docker service is active and enabled] ****************************
ok: [staging-server]

PLAY RECAP ********************************************************************
staging-server             : ok=6    changed=4    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

---

## 5. Official Deliverable Sign-Off
- **Configuration Specification**: Hardware, user, and network prerequisites defined.
- **Ansible Playbook & Inventory**: `inventory.ini` and `site.yml` created in repository.
- **First Execution Log**: Recorded showing 4 tasks changed and 0 errors.
