/**
 * Performance Review Portal — Main JavaScript
 * Presentation-only enhancements: password toggle, char counters, confirm dialogs,
 * mobile nav, slider fill, reduced-motion guard.
 */

document.addEventListener('DOMContentLoaded', function () {

    /* ------------------------------------------------------------------ */
    /* 1. Reduced-motion detection                                          */
    /* ------------------------------------------------------------------ */
    const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    /* ------------------------------------------------------------------ */
    /* 2. Password visibility toggle                                        */
    /* ------------------------------------------------------------------ */
    document.querySelectorAll('.btn-pw-toggle').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var input = document.getElementById(btn.dataset.target);
            if (!input) return;
            var isPassword = input.type === 'password';
            input.type = isPassword ? 'text' : 'password';
            var icon = btn.querySelector('i');
            if (icon) {
                icon.classList.toggle('bi-eye', !isPassword);
                icon.classList.toggle('bi-eye-slash', isPassword);
            }
            btn.setAttribute('aria-label', isPassword ? 'Hide password' : 'Show password');
        });
    });

    /* ------------------------------------------------------------------ */
    /* 3. Character counters for textareas                                  */
    /* ------------------------------------------------------------------ */
    document.querySelectorAll('textarea[data-max-chars]').forEach(function (ta) {
        var max = parseInt(ta.dataset.maxChars, 10);
        var counterId = ta.dataset.counterId;
        if (!counterId) return;
        var counter = document.getElementById(counterId);
        if (!counter) return;

        function updateCounter() {
            var remaining = max - ta.value.length;
            counter.textContent = remaining + ' characters remaining';
            counter.className = 'char-counter';
            if (remaining <= 50) counter.classList.add('near-limit');
            if (remaining <= 0) {
                counter.classList.add('at-limit');
                counter.textContent = '0 characters remaining';
            }
        }
        ta.addEventListener('input', updateCounter);
        updateCounter();
    });

    /* ------------------------------------------------------------------ */
    /* 4. Auto-dismiss flash alerts after 6 s                              */
    /* ------------------------------------------------------------------ */
    document.querySelectorAll('.alert.alert-dismissible').forEach(function (alert) {
        var delay = prefersReducedMotion ? 0 : 6000;
        setTimeout(function () {
            var closeBtn = alert.querySelector('.btn-close');
            if (closeBtn) closeBtn.click();
        }, delay);
    });

    /* ------------------------------------------------------------------ */
    /* 5. Slider track-fill (CSS custom property driven)                    */
    /* ------------------------------------------------------------------ */
    function updateSliderFill(range) {
        var min = parseFloat(range.min) || 1;
        var max = parseFloat(range.max) || 10;
        var val = parseFloat(range.value) || min;
        var pct = ((val - min) / (max - min)) * 100;
        range.style.setProperty('--fill-pct', pct + '%');
    }

    document.querySelectorAll('input[type="range"]').forEach(function (range) {
        updateSliderFill(range);
        range.addEventListener('input', function () { updateSliderFill(range); });
    });

    /* ------------------------------------------------------------------ */
    /* 6. Score badge micro-animation on slider change                      */
    /* ------------------------------------------------------------------ */
    if (!prefersReducedMotion) {
        document.querySelectorAll('input[type="range"]').forEach(function (range) {
            range.addEventListener('input', function () {
                var headerId = range.closest('.slider-group');
                if (!headerId) return;
                var badge = headerId.querySelector('.score-badge');
                if (!badge) return;
                badge.style.transform = 'scale(1.12)';
                setTimeout(function () { badge.style.transform = ''; }, 150);
            });
        });
    }

    /* ------------------------------------------------------------------ */
    /* 7. Manager Confirm Dialog (approve / reject)                         */
    /* ------------------------------------------------------------------ */
    window.showConfirmDialog = function (action, callback) {
        // Remove existing overlay if any
        var existing = document.getElementById('confirmOverlay');
        if (existing) existing.remove();

        var isApprove = action === 'approve';
        var actionLabel = isApprove ? 'Approve' : 'Reject';
        var actionClass = isApprove ? 'btn btn-success' : 'btn btn-danger';
        var icon = isApprove ? 'bi-check-circle-fill' : 'bi-x-circle-fill';
        var msg = isApprove
            ? 'You are about to <strong>approve</strong> this review. The overall score will be saved and the employee will be notified.'
            : 'You are about to <strong>reject</strong> this review. This action will return the review to the employee.';

        var overlay = document.createElement('div');
        overlay.id = 'confirmOverlay';
        overlay.className = 'confirm-overlay';
        overlay.setAttribute('role', 'dialog');
        overlay.setAttribute('aria-modal', 'true');
        overlay.setAttribute('aria-labelledby', 'confirmTitle');
        overlay.innerHTML =
            '<div class="confirm-box">' +
            '  <i class="bi ' + icon + ' fs-1 mb-3 d-block ' + (isApprove ? 'text-success' : 'text-danger') + '"></i>' +
            '  <h5 class="fw-bold mb-2" id="confirmTitle">Confirm ' + actionLabel + '</h5>' +
            '  <p class="text-muted small mb-4">' + msg + '</p>' +
            '  <div class="d-flex gap-2 justify-content-center">' +
            '    <button type="button" class="btn btn-light border px-4" id="confirmCancel">Cancel</button>' +
            '    <button type="button" class="' + actionClass + ' px-4" id="confirmOk">' + actionLabel + '</button>' +
            '  </div>' +
            '</div>';

        document.body.appendChild(overlay);

        var cancelBtn = document.getElementById('confirmCancel');
        var okBtn = document.getElementById('confirmOk');

        cancelBtn.focus();

        function closeOverlay() {
            if (overlay.parentNode) overlay.parentNode.removeChild(overlay);
        }

        cancelBtn.addEventListener('click', closeOverlay);
        okBtn.addEventListener('click', function () {
            closeOverlay();
            if (typeof callback === 'function') callback();
        });

        // Close on Escape
        overlay.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') { closeOverlay(); }
        });
        // Close on backdrop click
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) closeOverlay();
        });
    };

    /* ------------------------------------------------------------------ */
    /* 8. Mobile nav toggle (if a hamburger button exists)                  */
    /* ------------------------------------------------------------------ */
    var navToggle = document.getElementById('mobileNavToggle');
    var navMenu   = document.getElementById('mobileNavMenu');
    if (navToggle && navMenu) {
        navToggle.addEventListener('click', function () {
            var expanded = navToggle.getAttribute('aria-expanded') === 'true';
            navToggle.setAttribute('aria-expanded', String(!expanded));
            navMenu.classList.toggle('d-none');
        });
    }

    /* ------------------------------------------------------------------ */
    /* 9. Form submission — show loading state on primary buttons           */
    /* ------------------------------------------------------------------ */
    document.querySelectorAll('form').forEach(function (form) {
        form.addEventListener('submit', function () {
            var btn = form.querySelector('button[type="submit"]');
            if (!btn || btn.disabled) return;
            // Don't lock manager eval form — it uses custom submitEvaluation()
            if (form.id === 'evalForm') return;
            btn.disabled = true;
            var span = btn.querySelector('span');
            if (span) span.textContent = 'Processing…';
        });
    });

    console.log('Performance Review Portal initialized.');
});
