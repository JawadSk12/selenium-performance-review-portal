import os
import sys
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.enum.text import PP_ALIGN
from pptx.dml.color import RGBColor
from pptx.enum.shapes import MSO_SHAPE

def create_deck():
    prs = Presentation()
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6] # Blank slide

    # Color Palette Definitions
    DARK_BG = RGBColor(15, 23, 42)       # Slate 900
    LIGHT_BG = RGBColor(248, 250, 252)   # Slate 50
    CARD_BG = RGBColor(255, 255, 255)    # White
    CARD_BORDER = RGBColor(226, 232, 240)# Slate 200
    
    PRIMARY_INDIGO = RGBColor(79, 70, 229) # Indigo 600
    ACCENT_BLUE = RGBColor(14, 165, 233)   # Sky 500
    ACCENT_EMERALD = RGBColor(16, 185, 129)# Emerald 500
    ACCENT_AMBER = RGBColor(245, 158, 11)  # Amber 500
    
    TEXT_DARK = RGBColor(15, 23, 42)       # Slate 900
    TEXT_BODY = RGBColor(51, 65, 85)       # Slate 700
    TEXT_MUTED = RGBColor(100, 116, 139)   # Slate 500
    TEXT_LIGHT = RGBColor(255, 255, 255)   # White
    TEXT_LIGHT_MUTED = RGBColor(148, 163, 184) # Slate 400

    def add_header(slide, badge_text, title_text, subtitle_text, dark=False):
        # Header Badge
        badge_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.42), Inches(5.5), Inches(0.35))
        tf = badge_box.text_frame
        tf.word_wrap = True
        tf.margin_left = tf.margin_top = tf.margin_right = tf.margin_bottom = 0
        p = tf.paragraphs[0]
        p.text = badge_text.upper()
        p.font.size = Pt(10)
        p.font.bold = True
        p.font.color.rgb = PRIMARY_INDIGO if not dark else ACCENT_BLUE

        # Title
        title_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.75), Inches(11.5), Inches(0.65))
        tf2 = title_box.text_frame
        tf2.word_wrap = True
        tf2.margin_left = tf2.margin_top = tf2.margin_right = tf2.margin_bottom = 0
        p2 = tf2.paragraphs[0]
        p2.text = title_text
        p2.font.size = Pt(24)
        p2.font.bold = True
        p2.font.color.rgb = TEXT_DARK if not dark else TEXT_LIGHT

        # Subtitle
        sub_box = slide.shapes.add_textbox(Inches(0.8), Inches(1.38), Inches(11.5), Inches(0.4))
        tf3 = sub_box.text_frame
        tf3.word_wrap = True
        tf3.margin_left = tf3.margin_top = tf3.margin_right = tf3.margin_bottom = 0
        p3 = tf3.paragraphs[0]
        p3.text = subtitle_text
        p3.font.size = Pt(13)
        p3.font.color.rgb = TEXT_MUTED if not dark else TEXT_LIGHT_MUTED

    def add_card(slide, left, top, width, height, bg_color=CARD_BG, border_color=CARD_BORDER):
        shape = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        shape.fill.solid()
        shape.fill.fore_color.rgb = bg_color
        if border_color:
            shape.line.color.rgb = border_color
            shape.line.width = Pt(1.2)
        else:
            shape.line.fill.background()
        return shape

    def add_footer(slide, current_slide, total_slides=10, dark=False):
        footer_box = slide.shapes.add_textbox(Inches(0.8), Inches(7.05), Inches(11.733), Inches(0.3))
        tf = footer_box.text_frame
        tf.word_wrap = True
        tf.margin_left = tf.margin_top = tf.margin_right = tf.margin_bottom = 0
        
        p = tf.paragraphs[0]
        p.text = "Selenium-Tested Performance Review Portal  |  Full-Stack & DevOps Engineering"
        p.font.size = Pt(9)
        p.font.color.rgb = TEXT_MUTED if not dark else TEXT_LIGHT_MUTED
        
        p2 = tf.add_paragraph()
        p2.text = f"Slide {current_slide} of {total_slides}"
        p2.alignment = PP_ALIGN.RIGHT
        p2.font.size = Pt(9)
        p2.font.bold = True
        p2.font.color.rgb = PRIMARY_INDIGO if not dark else ACCENT_BLUE

    # =========================================================================
    # SLIDE 1: TITLE SLIDE (Dark Theme)
    # =========================================================================
    s1 = prs.slides.add_slide(blank_layout)
    bg1 = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg1.fill.solid()
    bg1.fill.fore_color.rgb = DARK_BG
    bg1.line.fill.background()

    # Top accent bar
    top_bar = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(0.12))
    top_bar.fill.solid()
    top_bar.fill.fore_color.rgb = PRIMARY_INDIGO
    top_bar.line.fill.background()

    # Project Tag Badge
    badge1 = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(0.9), Inches(4.5), Inches(0.42))
    badge1.fill.solid()
    badge1.fill.fore_color.rgb = RGBColor(30, 41, 59)
    badge1.line.color.rgb = PRIMARY_INDIGO
    tf = badge1.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "ACADEMIC PROJECT PRESENTATION • DEVOPS"
    p.font.size = Pt(10)
    p.font.bold = True
    p.font.color.rgb = ACCENT_BLUE
    p.alignment = PP_ALIGN.CENTER

    # Title & Subtitle Box
    t_box = s1.shapes.add_textbox(Inches(0.8), Inches(1.5), Inches(11.7), Inches(2.3))
    tf = t_box.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Selenium-Tested Performance Review Portal"
    p.font.size = Pt(36)
    p.font.bold = True
    p.font.color.rgb = TEXT_LIGHT

    p2 = tf.add_paragraph()
    p2.text = "Automated Employee Appraisal Platform with Continuous Testing, Dockerization & Ansible Provisioning"
    p2.font.size = Pt(17)
    p2.font.color.rgb = ACCENT_BLUE

    # Student & College Meta Cards
    add_card(s1, Inches(0.8), Inches(4.1), Inches(5.6), Inches(2.5), RGBColor(30, 41, 59), RGBColor(51, 65, 85))
    s_box = s1.shapes.add_textbox(Inches(1.1), Inches(4.3), Inches(5.0), Inches(2.1))
    tf = s_box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = "CANDIDATE DETAILS"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = ACCENT_EMERALD

    items = [
        ("Student Name:", "Jawad Shaikh"),
        ("Program / Branch:", "Computer Engineering / IT"),
        ("Specialization:", "DevOps & Software Quality Engineering"),
        ("Project Repo:", "github.com/JawadSk12/selenium-performance-review-portal")
    ]
    for label, val in items:
        p = tf.add_paragraph()
        p.text = f"{label} "
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = TEXT_LIGHT_MUTED
        run = p.add_run()
        run.text = val
        run.font.bold = False
        run.font.color.rgb = TEXT_LIGHT

    # Institution & Guide Meta Card
    add_card(s1, Inches(6.8), Inches(4.1), Inches(5.7), Inches(2.5), RGBColor(30, 41, 59), RGBColor(51, 65, 85))
    g_box = s1.shapes.add_textbox(Inches(7.1), Inches(4.3), Inches(5.1), Inches(2.1))
    tf = g_box.text_frame
    tf.word_wrap = True
    
    p = tf.paragraphs[0]
    p.text = "ACADEMIC & EVALUATION DETAILS"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = ACCENT_AMBER

    items2 = [
        ("Academic Year:", "2025 – 2026"),
        ("College Name:", "[Your College / University Name Placeholder]"),
        ("Department:", "Department of Computer Engineering"),
        ("Project Guide:", "[Project Guide / Mentor Name Placeholder]")
    ]
    for label, val in items2:
        p = tf.add_paragraph()
        p.text = f"{label} "
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = TEXT_LIGHT_MUTED
        run = p.add_run()
        run.text = val
        run.font.bold = False
        run.font.color.rgb = TEXT_LIGHT

    add_footer(s1, 1, 10, dark=True)

    # =========================================================================
    # SLIDE 2: INTRODUCTION
    # =========================================================================
    s2 = prs.slides.add_slide(blank_layout)
    bg2 = s2.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg2.fill.solid()
    bg2.fill.fore_color.rgb = LIGHT_BG
    bg2.line.fill.background()

    add_header(s2, "Slide 2 • Project Overview", "Introduction to Performance Review Portal", "Modernizing enterprise employee appraisal through full-stack development and DevOps automation")

    # Column 1: Application Purpose
    add_card(s2, Inches(0.8), Inches(2.0), Inches(5.7), Inches(3.8))
    c1_box = s2.shapes.add_textbox(Inches(1.1), Inches(2.2), Inches(5.1), Inches(3.4))
    tf = c1_box.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "What is the Performance Review Portal?"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = PRIMARY_INDIGO

    bullets1 = [
        "A secure, web-based enterprise evaluation system designed for modern organizations.",
        "Facilitates structured, periodic performance reviews between employees and managers.",
        "Replaces disorganized paper appraisals and loose spreadsheets with a centralized database.",
        "Features two strictly segregated portals: Employee Portal and Manager Workspace.",
        "Delivers transparent self-ratings, feedback documentation, and status tracking."
    ]
    for b in bullets1:
        p = tf.add_paragraph()
        p.text = f"•  {b}"
        p.font.size = Pt(11.5)
        p.font.color.rgb = TEXT_BODY
        p.space_after = Pt(7)

    # Column 2: DevOps Engineering
    add_card(s2, Inches(6.8), Inches(2.0), Inches(5.7), Inches(3.8))
    c2_box = s2.shapes.add_textbox(Inches(7.1), Inches(2.2), Inches(5.1), Inches(3.4))
    tf = c2_box.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Why DevOps & Quality Automation?"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = ACCENT_BLUE

    bullets2 = [
        "Eliminates manual testing risks by automating end-to-end user journeys using Selenium WebDriver.",
        "Implements a continuous quality gate: builds must pass 100% of tests before deployment.",
        "Packages the application into immutable Docker container images for zero environment drift.",
        "Automates server provisioning and configuration management using idempotent Ansible playbooks.",
        "Demonstrates complete software development life cycle (SDLC) from commit to live runtime."
    ]
    for b in bullets2:
        p = tf.add_paragraph()
        p.text = f"•  {b}"
        p.font.size = Pt(11.5)
        p.font.color.rgb = TEXT_BODY
        p.space_after = Pt(7)

    # Bottom Callout Box
    add_card(s2, Inches(0.8), Inches(6.0), Inches(11.7), Inches(0.85), RGBColor(238, 242, 255), PRIMARY_INDIGO)
    callout = s2.shapes.add_textbox(Inches(1.1), Inches(6.12), Inches(11.1), Inches(0.6))
    tf = callout.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Key Takeaway: The project combines robust Spring Boot web development with real-world DevOps practices — automated regression testing, CI/CD pipelines, containerization, and configuration management."
    p.font.size = Pt(11.5)
    p.font.bold = True
    p.font.color.rgb = PRIMARY_INDIGO

    add_footer(s2, 2, 10)

    # =========================================================================
    # SLIDE 3: PROBLEM STATEMENT
    # =========================================================================
    s3 = prs.slides.add_slide(blank_layout)
    bg3 = s3.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg3.fill.solid()
    bg3.fill.fore_color.rgb = LIGHT_BG
    bg3.line.fill.background()

    add_header(s3, "Slide 3 • The Challenge", "Problem Statement & Industry Context", "Why traditional employee appraisal and manual deployment practices fail in modern IT teams")

    problems = [
        ("1. Manual & Unstructured Appraisals",
         "Spreadsheet Chaos & Data Loss",
         [
             "Performance reviews are conducted via disconnected spreadsheets, emails, and PDFs.",
             "Lack of centralized tracking causes lost records, skipped reviews, and audit failures.",
             "Employees have zero visibility into review status or historical performance growth."
         ],
         ACCENT_AMBER),
        ("2. Subjectivity & Review Bottlenecks",
         "Lack of Standard Metrics & Queues",
         [
             "Evaluations lack standardized scoring categories (technical vs teamwork vs delivery).",
             "Managers receive unsorted appraisal requests with no filtering or status management.",
             "Delay in approvals frustrates employees and slows promotion/increment cycles."
         ],
         PRIMARY_INDIGO),
        ("3. Fragile Releases & Environment Drift",
         "Manual Deployment Risks",
         [
             "Web applications deployed without automated UI regression tests break silently in production.",
             "'Works on my machine' syndrome creates discrepancies between dev, staging, and live servers.",
             "Manual server configuration leads to configuration drift, security flaws, and downtime."
         ],
         RGBColor(239, 68, 68)) # Red
    ]

    card_w = Inches(3.68)
    card_gap = Inches(0.33)
    start_x = Inches(0.8)

    for i, (title, sub, points, accent) in enumerate(problems):
        cx = start_x + i * (card_w + card_gap)
        add_card(s3, cx, Inches(2.0), card_w, Inches(4.8))
        
        # Header banner inside card
        header_shape = s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, cx, Inches(2.0), card_w, Inches(0.8))
        header_shape.fill.solid()
        header_shape.fill.fore_color.rgb = accent
        header_shape.line.fill.background()
        tf = header_shape.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = title
        p.font.size = Pt(12)
        p.font.bold = True
        p.font.color.rgb = TEXT_LIGHT
        p.alignment = PP_ALIGN.CENTER

        # Content Box
        body = s3.shapes.add_textbox(cx + Inches(0.2), Inches(3.0), card_w - Inches(0.4), Inches(3.6))
        tf = body.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = sub
        p.font.size = Pt(13)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        p.space_after = Pt(12)

        for pt in points:
            p = tf.add_paragraph()
            p.text = f"•  {pt}"
            p.font.size = Pt(11)
            p.font.color.rgb = TEXT_BODY
            p.space_after = Pt(9)

    add_footer(s3, 3, 10)

    # =========================================================================
    # SLIDE 4: OBJECTIVES
    # =========================================================================
    s4 = prs.slides.add_slide(blank_layout)
    bg4 = s4.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg4.fill.solid()
    bg4.fill.fore_color.rgb = LIGHT_BG
    bg4.line.fill.background()

    add_header(s4, "Slide 4 • Goals & Targets", "Project Objectives & Key Outcomes", "Specific targets established for application development, automated testing, and DevOps deployment")

    objectives = [
        ("Role-Based Web Application",
         "Develop two dedicated portals: Employee Self-Appraisal Portal and HR Manager Review Dashboard with strict HTTP interceptor route protection.",
         PRIMARY_INDIGO),
        ("Standardized Evaluation Matrix",
         "Implement a 1–10 numerical scoring system across 4 key competencies (Technical, Communication, Teamwork, Problem-Solving) plus qualitative feedback.",
         ACCENT_BLUE),
        ("Automated Quality Assurance Gate",
         "Author 23 comprehensive automated tests (Spring Boot unit/integration tests + Selenium WebDriver browser tests) running on every build.",
         ACCENT_EMERALD),
        ("Full CI/CD Pipeline Integration",
         "Implement a multi-stage Jenkins pipeline that automates checkout, compilation, packaging, continuous testing, and artifact deployment.",
         ACCENT_AMBER),
        ("Containerization & Isolation",
         "Dockerize the application into a standalone Linux container with PostgreSQL integration, ensuring zero dependency conflicts.",
         PRIMARY_INDIGO),
        ("Automated Server Provisioning",
         "Author idempotent Ansible playbooks (site.yml & week14-provision.yml) to configure production target nodes and verify reliability.",
         RGBColor(16, 185, 129))
    ]

    grid_w = Inches(3.7)
    grid_h = Inches(2.25)
    xs = [Inches(0.8), Inches(4.8), Inches(8.8)]
    ys = [Inches(2.0), Inches(4.55)]

    for idx, (head, desc, color) in enumerate(objectives):
        col = idx % 3
        row = idx // 3
        cx = xs[col]
        cy = ys[row]

        add_card(s4, cx, cy, grid_w, grid_h)
        
        # Icon / Number marker
        marker = s4.shapes.add_shape(MSO_SHAPE.OVAL, cx + Inches(0.25), cy + Inches(0.25), Inches(0.45), Inches(0.45))
        marker.fill.solid()
        marker.fill.fore_color.rgb = color
        marker.line.fill.background()
        tf = marker.text_frame
        p = tf.paragraphs[0]
        p.text = str(idx + 1)
        p.font.size = Pt(12)
        p.font.bold = True
        p.font.color.rgb = TEXT_LIGHT
        p.alignment = PP_ALIGN.CENTER

        # Text
        txt = s4.shapes.add_textbox(cx + Inches(0.85), cy + Inches(0.2), grid_w - Inches(1.05), grid_h - Inches(0.35))
        tf = txt.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = head
        p.font.size = Pt(13)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        p.space_after = Pt(6)

        p2 = tf.add_paragraph()
        p2.text = desc
        p2.font.size = Pt(10.5)
        p2.font.color.rgb = TEXT_BODY

    add_footer(s4, 4, 10)

    # =========================================================================
    # SLIDE 5: PROPOSED SOLUTION
    # =========================================================================
    s5 = prs.slides.add_slide(blank_layout)
    bg5 = s5.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg5.fill.solid()
    bg5.fill.fore_color.rgb = LIGHT_BG
    bg5.line.fill.background()

    add_header(s5, "Slide 5 • Technical Approach", "Proposed Solution & System Architecture", "How the implemented system addresses all appraisal, testing, and deployment requirements")

    # Left: Workflow Sequence Card
    add_card(s5, Inches(0.8), Inches(2.0), Inches(5.6), Inches(4.8))
    left_tb = s5.shapes.add_textbox(Inches(1.0), Inches(2.2), Inches(5.2), Inches(4.4))
    tf = left_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "End-to-End System Workflow"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = PRIMARY_INDIGO
    p.space_after = Pt(12)

    flow_steps = [
        ("Step 1: Employee Self-Appraisal", "Employee logs in at /login, scores themselves (1–10) across 4 categories, records achievements and goals, and submits."),
        ("Step 2: Database Persistence & Queue", "Review is saved with status 'PENDING' in PostgreSQL and immediately routed to the manager's review queue."),
        ("Step 3: Manager Evaluation & Decision", "Manager logs in at /manager/login, inspects KPIs, filters candidates, provides feedback, and clicks Approve or Reject."),
        ("Step 4: Real-time Status Audit", "Employee history page updates with final decision, feedback notes, and timestamps for a complete audit trail."),
        ("Step 5: Automated Testing Quality Gate", "Selenium WebDriver simulates real browser actions verifying login, submission, and logout on every commit.")
    ]
    for step_title, step_desc in flow_steps:
        p = tf.add_paragraph()
        p.text = f"{step_title}: "
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        run = p.add_run()
        run.text = step_desc
        run.font.bold = False
        run.font.color.rgb = TEXT_BODY
        p.space_after = Pt(7)

    # Right: Architectural Pillars
    pillars = [
        ("1. Separate Authentication Interceptors",
         "Independent session interceptors ensure employees cannot access /manager/** and unauthenticated users are redirected to login.",
         PRIMARY_INDIGO),
        ("2. Clean Spring Boot MVC Architecture",
         "Strict separation of concerns: Model (Entities), View (Thymeleaf Templates), Controller (REST/Web handlers), and Service layer.",
         ACCENT_BLUE),
        ("3. Continuous Quality Assurance",
         "23 automated Maven tests execute on every build. If any Selenium UI test fails, the CI pipeline halts immediately.",
         ACCENT_EMERALD),
        ("4. Immutable Docker Deployment",
         "Packaged as a standalone container image (performance-review-portal:week11) on port 8082 with reproducible runtime.",
         ACCENT_AMBER)
    ]

    p_w = Inches(5.8)
    p_h = Inches(1.1)
    p_start_y = Inches(2.0)
    p_gap = Inches(0.13)

    for i, (p_title, p_desc, p_color) in enumerate(pillars):
        cy = p_start_y + i * (p_h + p_gap)
        add_card(s5, Inches(6.7), cy, p_w, p_h)
        
        # Left color bar
        bar = s5.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(6.7), cy, Inches(0.18), p_h)
        bar.fill.solid()
        bar.fill.fore_color.rgb = p_color
        bar.line.fill.background()

        tb = s5.shapes.add_textbox(Inches(7.05), cy + Inches(0.12), p_w - Inches(0.5), p_h - Inches(0.24))
        tf = tb.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = p_title
        p.font.size = Pt(12)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        p.space_after = Pt(4)

        p2 = tf.add_paragraph()
        p2.text = p_desc
        p2.font.size = Pt(10)
        p2.font.color.rgb = TEXT_BODY

    add_footer(s5, 5, 10)

    # =========================================================================
    # SLIDE 6: KEY FEATURES
    # =========================================================================
    s6 = prs.slides.add_slide(blank_layout)
    bg6 = s6.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg6.fill.solid()
    bg6.fill.fore_color.rgb = LIGHT_BG
    bg6.line.fill.background()

    add_header(s6, "Slide 6 • Functional Capabilities", "Key Features Implemented in the Portal", "Practical modules delivered for employees, HR managers, and system administrators")

    # Column 1: Employee Features
    add_card(s6, Inches(0.8), Inches(2.0), Inches(5.7), Inches(4.8))
    e_tb = s6.shapes.add_textbox(Inches(1.1), Inches(2.2), Inches(5.1), Inches(4.4))
    tf = e_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Employee Portal (/login)"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = PRIMARY_INDIGO
    p.space_after = Pt(10)

    e_feats = [
        ("Dedicated Employee Auth", "Clean login screen with credential validation and session state management."),
        ("4-Factor Self-Review Form", "Quantitative rating (1–10) across Technical, Communication, Teamwork, and Problem Solving."),
        ("Qualitative Accomplishments", "Detailed multi-line inputs for yearly achievements and future professional goals."),
        ("Live Status Indicators", "Dynamic status badge on dashboard indicating 'Pending Review', 'Completed', or 'No Reviews'."),
        ("Audit History View", "Historical table displaying all submitted reviews, manager ratings, and feedback comments.")
    ]
    for ft, fd in e_feats:
        p = tf.add_paragraph()
        p.text = f"•  {ft}: "
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        run = p.add_run()
        run.text = fd
        run.font.bold = False
        run.font.color.rgb = TEXT_BODY
        p.space_after = Pt(6)

    # Column 2: Manager Features
    add_card(s6, Inches(6.8), Inches(2.0), Inches(5.7), Inches(4.8))
    m_tb = s6.shapes.add_textbox(Inches(7.1), Inches(2.2), Inches(5.1), Inches(4.4))
    tf = m_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Manager Workspace (/manager/login)"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = ACCENT_BLUE
    p.space_after = Pt(10)

    m_feats = [
        ("Executive KPI Dashboard", "Instant overview of Total Reviews, Pending Approvals, Approved, and Rejected reviews."),
        ("Instant Employee Search", "Client-side JavaScript search filter that filters pending reviews by employee name in real time."),
        ("Comprehensive Review View", "Detailed evaluation view (/manager/review/{id}) showing employee self-scores and goals."),
        ("Approval Decision Gate", "One-click 'Approve' or 'Reject' workflow with required managerial comment recording."),
        ("Secure Route Protection", "ManagerAuthInterceptor redirects unauthorized URL tampering directly back to login.")
    ]
    for ft, fd in m_feats:
        p = tf.add_paragraph()
        p.text = f"•  {ft}: "
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        run = p.add_run()
        run.text = fd
        run.font.bold = False
        run.font.color.rgb = TEXT_BODY
        p.space_after = Pt(6)

    add_footer(s6, 6, 10)

    # =========================================================================
    # SLIDE 7: TECHNOLOGY STACK
    # =========================================================================
    s7 = prs.slides.add_slide(blank_layout)
    bg7 = s7.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg7.fill.solid()
    bg7.fill.fore_color.rgb = LIGHT_BG
    bg7.line.fill.background()

    add_header(s7, "Slide 7 • Tools & Technologies", "Technology Stack & Development Ecosystem", "A full-stack, enterprise Java technology stack paired with industry-standard DevOps tools")

    tech_categories = [
        ("Backend Framework",
         "Core Application Engine",
         [
             ("Java 17 (LTS)", "Modern, high-performance object-oriented foundation"),
             ("Spring Boot 3.3.3", "Rapid enterprise MVC framework with embedded Tomcat"),
             ("Spring Data JPA", "Object-relational mapping for PostgreSQL entities"),
             ("Hibernate 6", "ORM provider handling schema generation and transactions")
         ],
         PRIMARY_INDIGO),
        ("Frontend & Presentation",
         "User Interface & Aesthetics",
         [
             ("Thymeleaf Engine", "Server-side HTML rendering with dynamic expressions"),
             ("HTML5 & Vanilla CSS", "Custom responsive styling and modern cards"),
             ("Bootstrap 5.3", "Responsive layout grid and modern UI components"),
             ("Vanilla JavaScript", "Client-side instant search filtering and validations")
         ],
         ACCENT_BLUE),
        ("Database & Testing",
         "Persistence & Quality Gates",
         [
             ("PostgreSQL 16", "ACID-compliant relational database on port 5432"),
             ("Selenium WebDriver", "Automated browser end-to-end testing (v4.19.1)"),
             ("JUnit 5 & Mockito", "Unit and integration test runner with mocking"),
             ("Maven Surefire", "Automated test execution and XML report generation")
         ],
         ACCENT_EMERALD),
        ("DevOps & Infrastructure",
         "CI/CD & Provisioning",
         [
             ("Jenkins CI/CD", "Automated 6-stage build and testing pipeline (port 8080)"),
             ("Docker & Compose", "Container packaging (port 8082) & multi-container setup"),
             ("Ansible (WSL)", "Idempotent server provisioning (site.yml, week14-provision)"),
             ("Git & GitHub", "Source version control with develop branch workflow")
         ],
         ACCENT_AMBER)
    ]

    card_w7 = Inches(2.78)
    card_gap7 = Inches(0.2)
    start_x7 = Inches(0.8)

    for i, (cat_name, cat_sub, tools, accent) in enumerate(tech_categories):
        cx = start_x7 + i * (card_w7 + card_gap7)
        add_card(s7, cx, Inches(2.0), card_w7, Inches(4.8))
        
        # Header banner
        h_shape = s7.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, cx, Inches(2.0), card_w7, Inches(0.75))
        h_shape.fill.solid()
        h_shape.fill.fore_color.rgb = accent
        h_shape.line.fill.background()
        tf = h_shape.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = cat_name
        p.font.size = Pt(12)
        p.font.bold = True
        p.font.color.rgb = TEXT_LIGHT
        p.alignment = PP_ALIGN.CENTER

        # Subtitle
        tb = s7.shapes.add_textbox(cx + Inches(0.15), Inches(2.85), card_w7 - Inches(0.3), Inches(3.8))
        tf = tb.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = cat_sub.upper()
        p.font.size = Pt(9.5)
        p.font.bold = True
        p.font.color.rgb = TEXT_MUTED
        p.space_after = Pt(10)

        for name, desc in tools:
            p = tf.add_paragraph()
            p.text = name
            p.font.size = Pt(11)
            p.font.bold = True
            p.font.color.rgb = TEXT_DARK
            
            p2 = tf.add_paragraph()
            p2.text = desc
            p2.font.size = Pt(9.5)
            p2.font.color.rgb = TEXT_BODY
            p2.space_after = Pt(6)

    add_footer(s7, 7, 10)

    # =========================================================================
    # SLIDE 8: SYSTEM ARCHITECTURE & PIPELINE WORKFLOW
    # =========================================================================
    s8 = prs.slides.add_slide(blank_layout)
    bg8 = s8.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg8.fill.solid()
    bg8.fill.fore_color.rgb = LIGHT_BG
    bg8.line.fill.background()

    add_header(s8, "Slide 8 • System Design & CI/CD", "System Architecture & DevOps Pipeline", "High-level MVC system architecture and automated continuous delivery workflow")

    # Left: Architecture Diagram / Components
    add_card(s8, Inches(0.8), Inches(2.0), Inches(5.8), Inches(4.8))
    
    arch_path = os.path.join("docs", "System_Architecture.png")
    if os.path.exists(arch_path):
        s8.shapes.add_picture(arch_path, Inches(1.0), Inches(2.15), width=Inches(5.4))

    # Caption under image
    cap_box = s8.shapes.add_textbox(Inches(1.0), Inches(6.15), Inches(5.4), Inches(0.5))
    tf = cap_box.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Figure 1: High-Level Architecture showing Client Browser, Spring MVC Controllers, Services, and PostgreSQL Database."
    p.font.size = Pt(9.5)
    p.font.color.rgb = TEXT_MUTED

    # Right: Jenkins CI/CD Pipeline Stages
    add_card(s8, Inches(6.8), Inches(2.0), Inches(5.7), Inches(4.8))
    p_tb = s8.shapes.add_textbox(Inches(7.05), Inches(2.2), Inches(5.2), Inches(4.4))
    tf = p_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "6-Stage Automated CI/CD Pipeline"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = PRIMARY_INDIGO
    p.space_after = Pt(8)

    stages = [
        ("Stage 1: Checkout", "Jenkins pulls clean code from GitHub develop branch via SCM plugin."),
        ("Stage 2: Build", "Executes 'mvn clean compile' to verify Java syntax and class compilation."),
        ("Stage 3: Prepare Test App", "Packages WAR and deploys to Tomcat on port 8081 for live test execution."),
        ("Stage 4: Continuous Testing", "Runs unit tests and Selenium browser tests ('mvn test') — 100% pass required."),
        ("Stage 5: Test Reports", "Publishes JUnit test XML reports to Jenkins dashboard as permanent build artifacts."),
        ("Stage 6: Docker & Provisioning", "Builds Docker image week11 (port 8082) and verifies server with Ansible.")
    ]
    for s_title, s_desc in stages:
        p = tf.add_paragraph()
        p.text = f"{s_title}: "
        p.font.size = Pt(10.5)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        run = p.add_run()
        run.text = s_desc
        run.font.bold = False
        run.font.color.rgb = TEXT_BODY
        p.space_after = Pt(5)

    add_footer(s8, 8, 10)

    # =========================================================================
    # SLIDE 9: IMPLEMENTATION RESULTS & TEST EVIDENCE
    # =========================================================================
    s9 = prs.slides.add_slide(blank_layout)
    bg9 = s9.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg9.fill.solid()
    bg9.fill.fore_color.rgb = LIGHT_BG
    bg9.line.fill.background()

    add_header(s9, "Slide 9 • Verification & Outputs", "Implementation Results & Test Evidence", "Live application screenshots, 100% automated test pass rate, and container health verification")

    # Image 1: Manager Dashboard Screenshot
    add_card(s9, Inches(0.8), Inches(2.0), Inches(5.7), Inches(3.4))
    img1_path = os.path.join("docs", "ManagerDashboard.png")
    if os.path.exists(img1_path):
        s9.shapes.add_picture(img1_path, Inches(0.9), Inches(2.1), width=Inches(5.5))
    
    cap1 = s9.shapes.add_textbox(Inches(0.9), Inches(5.15), Inches(5.5), Inches(0.3))
    tf = cap1.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Output 1: Manager Workspace (/manager/dashboard) with live KPI cards and pending review queue"
    p.font.size = Pt(9)
    p.font.bold = True
    p.font.color.rgb = TEXT_MUTED

    # Image 2: Review Evaluation Screenshot
    add_card(s9, Inches(6.8), Inches(2.0), Inches(5.7), Inches(3.4))
    img2_path = os.path.join("docs", "ReviewEvaluation.png")
    if os.path.exists(img2_path):
        s9.shapes.add_picture(img2_path, Inches(6.9), Inches(2.1), width=Inches(5.5))
        
    cap2 = s9.shapes.add_textbox(Inches(6.9), Inches(5.15), Inches(5.5), Inches(0.3))
    tf = cap2.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Output 2: Detailed Manager Evaluation Interface (/manager/review/{id}) with Approve/Reject decision gate"
    p.font.size = Pt(9)
    p.font.bold = True
    p.font.color.rgb = TEXT_MUTED

    # Bottom Metric Cards
    metrics = [
        ("23 / 23 Tests Passed", "0 Failures, 0 Errors across JUnit & Selenium", ACCENT_EMERALD),
        ("Port 8082 Dockerized", "performance-review-portal:week11 active", PRIMARY_INDIGO),
        ("Ansible Idempotency", "ok=24, changed=0 on repeat play execution", ACCENT_BLUE)
    ]
    card_w9 = Inches(3.7)
    card_gap9 = Inches(0.3)
    for i, (m_head, m_sub, m_col) in enumerate(metrics):
        cx = Inches(0.8) + i * (card_w9 + card_gap9)
        add_card(s9, cx, Inches(5.65), card_w9, Inches(1.15))
        
        # Color accent strip
        strip = s9.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, cx, Inches(5.65), Inches(0.15), Inches(1.15))
        strip.fill.solid()
        strip.fill.fore_color.rgb = m_col
        strip.line.fill.background()

        tb = s9.shapes.add_textbox(cx + Inches(0.3), Inches(5.75), card_w9 - Inches(0.4), Inches(0.95))
        tf = tb.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = m_head
        p.font.size = Pt(13)
        p.font.bold = True
        p.font.color.rgb = TEXT_DARK
        
        p2 = tf.add_paragraph()
        p2.text = m_sub
        p2.font.size = Pt(10)
        p2.font.color.rgb = TEXT_BODY

    add_footer(s9, 9, 10)

    # =========================================================================
    # SLIDE 10: CONCLUSION & FUTURE SCOPE (Dark Theme / Finale)
    # =========================================================================
    s10 = prs.slides.add_slide(blank_layout)
    bg10 = s10.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg10.fill.solid()
    bg10.fill.fore_color.rgb = DARK_BG
    bg10.line.fill.background()

    # Top accent bar
    top_bar10 = s10.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(0.12))
    top_bar10.fill.solid()
    top_bar10.fill.fore_color.rgb = PRIMARY_INDIGO
    top_bar10.line.fill.background()

    add_header(s10, "Slide 10 • Summary & Looking Ahead", "Conclusion & Future Enhancements", "Project achievements, future feature scope, and closing remarks", dark=True)

    # Column 1: Achievements
    add_card(s10, Inches(0.8), Inches(2.0), Inches(5.7), Inches(3.6), RGBColor(30, 41, 59), RGBColor(51, 65, 85))
    ach_tb = s10.shapes.add_textbox(Inches(1.1), Inches(2.2), Inches(5.1), Inches(3.2))
    tf = ach_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Key Project Achievements"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = ACCENT_EMERALD
    p.space_after = Pt(8)

    achievements = [
        "Delivered a fully functional, secure web application with separate Employee and Manager portals.",
        "Implemented strict route protection and session isolation preventing cross-role access.",
        "Created an automated quality gate with 23 JUnit and Selenium tests (100% pass rate).",
        "Orchestrated a 6-stage Jenkins CI pipeline automating packaging and testing on every commit.",
        "Successfully containerized with Docker and verified server configuration using Ansible."
    ]
    for ach in achievements:
        p = tf.add_paragraph()
        p.text = f"✓  {ach}"
        p.font.size = Pt(11)
        p.font.color.rgb = TEXT_LIGHT
        p.space_after = Pt(5)

    # Column 2: Future Scope
    add_card(s10, Inches(6.8), Inches(2.0), Inches(5.7), Inches(3.6), RGBColor(30, 41, 59), RGBColor(51, 65, 85))
    fut_tb = s10.shapes.add_textbox(Inches(7.1), Inches(2.2), Inches(5.1), Inches(3.2))
    tf = fut_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Future Scope & Enhancements"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = ACCENT_BLUE
    p.space_after = Pt(8)

    future_items = [
        ("Automated Email / Slack Alerts", "Send immediate notifications to managers when reviews are submitted."),
        ("360-Degree Peer Feedback", "Expand evaluation matrix to allow multi-directional reviews from peer engineers."),
        ("AI-Powered Performance Insights", "Integrate LLM summarization to generate objective review syntheses."),
        ("Kubernetes Production Deployment", "Auto-scale container pods on AWS EKS / Kubernetes clusters."),
        ("Single Sign-On (SSO / OAuth2)", "Integrate corporate Google Workspace and Azure Active Directory login.")
    ]
    for f_title, f_desc in future_items:
        p = tf.add_paragraph()
        p.text = f"•  {f_title}: "
        p.font.size = Pt(10.5)
        p.font.bold = True
        p.font.color.rgb = ACCENT_BLUE
        run = p.add_run()
        run.text = f_desc
        run.font.bold = False
        run.font.color.rgb = TEXT_LIGHT_MUTED
        p.space_after = Pt(4)

    # Thank you banner
    add_card(s10, Inches(0.8), Inches(5.8), Inches(11.7), Inches(1.05), RGBColor(30, 41, 59), PRIMARY_INDIGO)
    ty_tb = s10.shapes.add_textbox(Inches(1.1), Inches(5.9), Inches(11.1), Inches(0.85))
    tf = ty_tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = "Thank You! Questions & Discussion"
    p.font.size = Pt(20)
    p.font.bold = True
    p.font.color.rgb = TEXT_LIGHT
    p.alignment = PP_ALIGN.CENTER

    p2 = tf.add_paragraph()
    p2.text = "Presented by: Jawad Shaikh  •  Selenium-Tested Performance Review Portal"
    p2.font.size = Pt(11)
    p2.font.color.rgb = ACCENT_BLUE
    p2.alignment = PP_ALIGN.CENTER

    add_footer(s10, 10, 10, dark=True)

    # Save presentation
    output_filename = "Performance_Review_Portal_Presentation.pptx"
    prs.save(output_filename)
    print(f"Presentation saved successfully as '{output_filename}' with {len(prs.slides)} slides.")

if __name__ == "__main__":
    create_deck()
