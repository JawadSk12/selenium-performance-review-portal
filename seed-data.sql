-- ===================================================================
-- Performance Review Portal - Demo Enterprise Seed Data Script
-- Run via: psql -U postgres -d performance_review -f seed-data.sql
-- ===================================================================

-- 1. Ensure Manager exists
INSERT INTO manager (name, email, password, department)
VALUES ('Ahmed Khan', 'manager@gmail.com', '1234', 'IT Management')
ON CONFLICT (email) DO UPDATE 
SET department = EXCLUDED.department;

-- 2. Insert Core Demo Employees (all default passwords = 1234)
INSERT INTO employee (name, email, password, department, designation, employee_code, manager_id)
VALUES 
  ('Jawad Shaikh',     'jawad@gmail.com',         '1234', 'IT',                   'Software Intern',           'EMP-1001', 1),
  ('Aisha Patel',      'aisha.patel@company.com',  '1234', 'Cloud Infrastructure', 'Cloud Solutions Engineer',   'EMP-1002', 1),
  ('Carlos Rodriguez', 'carlos.r@company.com',     '1234', 'Frontend & Mobile',    'Senior UI/UX Engineer',     'EMP-1003', 1),
  ('Emily Watson',     'emily.watson@company.com', '1234', 'Quality Assurance',    'QA Automation Lead',        'EMP-1004', 1),
  ('Michael Chang',    'michael.c@company.com',    '1234', 'Backend Engineering',  'Software Engineer II',      'EMP-1005', 1),
  ('Priya Sharma',     'priya.s@company.com',      '1234', 'DevOps & Security',    'Site Reliability Engineer', 'EMP-1006', 1)
ON CONFLICT (email) DO NOTHING;

-- 3. Pending Reviews (Appear in Manager's Queue for live evaluation)
INSERT INTO review (emp_id, manager_id, technical, communication, teamwork, problem_solving, achievement, future_goal, overall_score, status, review_date)
SELECT e.emp_id, 1, 9, 8, 9, 9, 
       'Architected multi-region failover and reduced AWS cloud infrastructure spend by 24%.', 
       'Attain Certified Kubernetes Administrator (CKA) and lead migration to Kubernetes 1.30.', 
       8.75, 'PENDING', CURRENT_DATE
FROM employee e WHERE e.email = 'aisha.patel@company.com'
AND NOT EXISTS (SELECT 1 FROM review r WHERE r.emp_id = e.emp_id AND r.status = 'PENDING');

INSERT INTO review (emp_id, manager_id, technical, communication, teamwork, problem_solving, achievement, future_goal, overall_score, status, review_date)
SELECT e.emp_id, 1, 8, 9, 9, 8, 
       'Engineered unified enterprise design system and achieved WCAG 2.1 AA accessibility compliance across all customer portals.', 
       'Pioneer micro-frontend architecture and improve web performance lighthouse scores to 95+.', 
       8.50, 'PENDING', CURRENT_DATE
FROM employee e WHERE e.email = 'carlos.r@company.com'
AND NOT EXISTS (SELECT 1 FROM review r WHERE r.emp_id = e.emp_id AND r.status = 'PENDING');

INSERT INTO review (emp_id, manager_id, technical, communication, teamwork, problem_solving, achievement, future_goal, overall_score, status, review_date)
SELECT e.emp_id, 1, 9, 9, 8, 10, 
       'Designed automated CI/CD pipeline using Jenkins and Ansible resulting in zero-downtime blue-green deployments.', 
       'Implement GitOps workflow with automated canary rollbacks and distributed tracing.', 
       9.00, 'PENDING', CURRENT_DATE
FROM employee e WHERE e.email = 'priya.s@company.com'
AND NOT EXISTS (SELECT 1 FROM review r WHERE r.emp_id = e.emp_id AND r.status = 'PENDING');

-- 4. Approved Reviews with Historical Feedback
INSERT INTO review (emp_id, manager_id, technical, communication, teamwork, problem_solving, achievement, future_goal, overall_score, status, review_date, approved_date, manager_comment)
SELECT e.emp_id, 1, 9, 9, 9, 9, 
       'Automated comprehensive regression testing with Selenium WebDriver, achieving 99.4% test suite stability.', 
       'Integrate automated performance stress testing with Apache JMeter into Jenkins CI.', 
       9.00, 'APPROVED', CURRENT_DATE - INTERVAL '14 days', CURRENT_DATE - INTERVAL '12 days',
       'Exceptional performance and technical rigor. Upheld high quality standards across releases.'
FROM employee e WHERE e.email = 'emily.watson@company.com'
AND NOT EXISTS (SELECT 1 FROM review r WHERE r.emp_id = e.emp_id AND r.status = 'APPROVED');

INSERT INTO review (emp_id, manager_id, technical, communication, teamwork, problem_solving, achievement, future_goal, overall_score, status, review_date, approved_date, manager_comment)
SELECT e.emp_id, 1, 8, 8, 8, 8, 
       'Refactored data persistence layer and optimized complex queries, reducing average API response latency by 38%.', 
       'Deepen reactive programming with Spring WebFlux and event streaming with Kafka.', 
       8.00, 'APPROVED', CURRENT_DATE - INTERVAL '21 days', CURRENT_DATE - INTERVAL '19 days',
       'Consistent contributor with solid technical fundamentals and great ownership of backend services.'
FROM employee e WHERE e.email = 'michael.c@company.com'
AND NOT EXISTS (SELECT 1 FROM review r WHERE r.emp_id = e.emp_id AND r.status = 'APPROVED');

