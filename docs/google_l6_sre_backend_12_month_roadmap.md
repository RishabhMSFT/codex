# 12-Month Roadmap: Google Staff Engineer (L6) Interview Prep (SRE + Backend + Infrastructure)

## 1) Daily 2-Hour Structured Schedule (Mon-Sun)

| Day | 120-minute structure | Output artifact |
|---|---|---|
| Monday (Theory) | 20m spaced review + 60m deep theory + 25m notes + 15m flashcards | 1-page concept map |
| Tuesday (Hands-on) | 15m recap + 80m lab + 25m write-up | Lab runbook + repo commit |
| Wednesday (Design) | 20m constraints + 70m system design + 30m critique | Design doc section |
| Thursday (Ops/Debug) | 15m incident scenario + 75m debugging drill + 30m postmortem | Timeline + RCA |
| Friday (Performance/Security) | 20m benchmark/threat model + 70m tuning + 30m findings | Perf/Sec report |
| Saturday (Build) | 15m planning + 90m production-grade project + 15m summary | Feature branch PR |
| Sunday (Interview) | 30m behavioral leadership + 45m system design mock + 45m infra deep-dive mock | Mock scorecard |

**Rules every day**
- Keep a `learning-log.md` with: topic, what changed in your mental model, open questions.
- End with one measurable metric or decision tradeoff statement.
- Weekly: publish one architecture note and one production runbook.

---

## 2) Monthly Themes, Objectives, Milestones

| Month | Theme | Objectives | Milestones |
|---|---|---|---|
| 1 | Linux + Networking Foundations | Build packet-to-process mental model | Explain TCP/TLS/DNS/traceroute from memory; debug with tcpdump/strace |
| 2 | Backend Reliability Patterns | Ship resilient API patterns | Implement idempotency, retries, rate limits, backpressure in service |
| 3 | Distributed Systems Core | Master replication/consensus tradeoffs | Design and defend Raft/Paxos choice + sharding model |
| 4 | Kubernetes + Containers Internals | Operate k8s beyond YAML | Debug CNI/Ingress/service failures under load |
| 5 | Observability at Scale | Build metrics/logs/traces platform | Golden signals + high-cardinality controls + actionable alerts |
| 6 | SRE Reliability Program | SLO/error-budget ops | Run incident game days + postmortem quality bar |
| 7 | IaC + CI/CD + Safe Delivery | Production automation maturity | GitOps + canary + blue/green + rollback automation |
| 8 | Performance + Capacity | p99 and cost/perf engineering | Capacity model and autoscaling math validated by load tests |
| 9 | Security Engineering | Shift-left + runtime defense | Zero trust, mTLS, secrets and supply-chain hardening |
| 10 | GCP/Cloud Architecture | Global infra design | Multi-region reference architecture with IAM + hybrid network |
| 11 | Staff Leadership Systems | Influence and architecture governance | Run cross-team reviews and migration program plan |
| 12 | Interview Simulation Month | Convert skill to interview signal | 4 full loops + polished project portfolio + STAR stories |

---

## 3) Domain-by-Domain Deep Plan (Why, Resources, Labs, Projects, Interview Qs)

> Use this format each week: **Learn -> Lab -> Ship -> Reflect -> Mock**.

### A) Distributed Systems
**Why it matters (L6):** Google expects principled tradeoff decisions under failure and scale.

- **Topics:** Raft/Paxos, CAP, replication, sharding, data modeling, multi-region active-active.
- **Resources:**
  - *Designing Data-Intensive Applications* (Kleppmann)
  - MIT 6.824 lectures
  - Google Spanner, Bigtable, and Chubby papers
  - Jepsen analyses (jepsen.io)
- **Labs:**
  - Build mini-Raft KV store with leader election and log replication.
  - Simulate network partition; document consistency/availability behavior.
- **Company-level projects:**
  1. Multi-region active-active order API with conflict resolution (CRDT/vector-clock based fallback).
     - Measurable outcomes: 99.95% regional failover continuity, RPO < 5s, p99 write latency < 120ms.
  2. Shard rebalancer service with hot-shard detection.
     - Outcomes: 40% hotspot reduction, 25% p99 improvement.
- **Google-style questions:**
  - “How would you design globally consistent counters?”
  - “When would you choose availability over consistency?”

### B) Backend Engineering
**Why it matters:** L6 backend interviews evaluate correctness, resilience, and operability.

- **Topics:** REST vs gRPC, idempotency/retries, rate limiting, backpressure, caching, async, DB internals.
- **Resources:**
  - gRPC docs + Google API Improvement Proposals
  - Martin Kleppmann DB chapters
  - High Scalability case studies
- **Labs:**
  - Implement token-bucket limiter + per-tenant quotas.
  - Add idempotency keys and dedupe store to payment-like endpoint.
- **Projects:**
  1. Unified API gateway (REST external, gRPC internal) with retry budgets.
     - Outcomes: 60% fewer duplicate operations, 35% lower error rate during retries.
  2. Async job platform with dead-letter queues and replay tooling.
     - Outcomes: MTTR for stuck jobs reduced by 50%.
- **Questions:**
  - “Design idempotent write APIs for unstable clients.”
  - “How do WAL and isolation levels affect latency and anomalies?”

### C) Networking & Infrastructure Internals
**Why it matters:** SRE/Infra rounds often probe packet-level understanding.

- **Topics:** OSI/TCP-IP, TCP congestion, TLS, DNS, traceroute, NAT/conntrack, LB, reverse proxy, WAF, DDoS, anycast, BGP.
- **Resources:**
  - Cloudflare blog (TLS, BGP, DDoS)
  - Kurose & Ross selected chapters
  - Julia Evans zines (networking)
- **Labs:**
  - Capture full TLS handshake with tcpdump/Wireshark and annotate each flight.
  - Reproduce SYN flood in lab; configure mitigation and compare baseline.
- **Projects:**
  1. Edge traffic protection stack (WAF + rate-limit + bot heuristics).
     - Outcomes: 70% malicious traffic blocked, <1% false positives.
  2. L4/L7 LB benchmark suite with consistent-hash routing.
     - Outcomes: 20% better cache locality, 15% lower tail latency.
- **Questions:**
  - “Why does traceroute work?”
  - “Anycast vs DNS steering tradeoffs for global APIs?”

### D) Linux & Kernel
**Why it matters:** Debugging rounds require syscall and scheduler fluency.

- **Resources:** *Linux Performance* (Brendan Gregg), man pages, kernel docs.
- **Labs:** epoll-based server; perf flamegraphs; strace-based latency triage.
- **Project:** eBPF network latency profiler for service mesh path.
  - Outcomes: identify top-3 kernel hotspots; 30% syscall overhead reduction.
- **Questions:**
  - “epoll vs select/poll?”
  - “How can fd leaks crash a high-QPS service?”

### E) Kubernetes & Containers
**Why it matters:** Infra leadership expects internals + production troubleshooting.

- **Resources:** Kubernetes docs, containerd architecture docs, Istio docs.
- **Labs:** inspect namespaces/cgroups manually; break CNI and recover; debug Ingress path.
- **Projects:**
  1. Multi-cluster k8s platform with traffic failover and policy guardrails.
     - Outcomes: regional failover under 3 minutes, 90% fewer manual failover steps.
  2. Autoscaling optimizer (HPA/VPA + custom metrics).
     - Outcomes: 22% infra cost reduction, SLO maintained.
- **Questions:**
  - “How does kube-proxy/ipvs route service traffic?”
  - “When does service mesh hurt latency?”

### F) IaC & Automation
**Why it matters:** Staff candidates are expected to scale delivery systems safely.

- **Resources:** Terraform internals docs, Helm source, ArgoCD/Flux docs.
- **Labs:** Terraform state drift simulation; Helm chart dependency issue debugging.
- **Projects:**
  1. GitOps release platform with canary + automated rollback.
     - Outcomes: change failure rate -40%, deployment frequency +2x.
  2. Environment bootstrap automation (Ansible + Terraform modules).
     - Outcomes: provisioning time from days to <2 hours.
- **Questions:**
  - “How do you design blast-radius controls in CI/CD?”

### G) SRE & Reliability Engineering
**Why it matters:** Core L6 SRE signal is reliability strategy, not only incident heroics.

- **Resources:** Google SRE books, Workbook, CRE life lessons.
- **Labs:** derive SLIs from user journey; error budget policy simulations.
- **Projects:**
  1. Org-wide SLO framework and alert redesign.
     - Outcomes: alert noise -50%, pages/actionable ratio +3x.
  2. Incident command playbook + postmortem standard.
     - Outcomes: MTTR -35%, repeat incidents -30%.
- **Questions:**
  - “How do you enforce error budgets with product teams?”

### H) Observability
**Why it matters:** Staff engineers define telemetry architecture and cost tradeoffs.

- **Resources:** Prometheus design docs, OpenTelemetry specs, Honeycomb blogs.
- **Labs:** high-cardinality stress test; trace sampling strategy comparison.
- **Projects:**
  1. Unified telemetry pipeline (metrics/logs/traces) with OTel collector tiers.
     - Outcomes: MTTD -40%, observability cost per service -25%.
- **Questions:**
  - “How do you control cardinality explosions without losing debuggability?”

### I) Performance & Capacity
**Why it matters:** L6 requires quantitative reasoning under growth and failures.

- **Resources:** *Systems Performance 2e*, Gil Tene talks, SRE capacity chapters.
- **Labs:** Little’s Law worksheet for each critical service; p50/p95/p99 regression analysis.
- **Projects:**
  1. Capacity forecasting model integrated with autoscaling guardrails.
     - Outcomes: forecast MAPE < 10%, emergency scale events -60%.
  2. Tail-latency reduction program.
     - Outcomes: p99 from 900ms -> 350ms at same throughput.
- **Questions:**
  - “Why can average latency improve while user experience worsens?”

### J) Security Engineering
**Why it matters:** Modern infra interviews include security-by-design decisions.

- **Resources:** OWASP Top 10, SPIFFE/SPIRE docs, NIST zero-trust.
- **Labs:** mTLS between services, secrets rotation drill, SAST/DAST pipeline setup.
- **Projects:**
  1. Zero-trust service-to-service auth plane (OIDC + mTLS + policy engine).
     - Outcomes: 100% encrypted east-west traffic, secret rotation <24h.
  2. Software supply-chain hardening (SBOM + signing + provenance).
     - Outcomes: critical vuln exposure window -70%.
- **Questions:**
  - “How would you secure a multi-tenant control plane?”

### K) Cloud Architecture (GCP-focused)
**Why it matters:** Google expects cloud-native and first-principles architecture.

- **Resources:** Google Cloud Architecture Center, BeyondProd papers, IAM best practices.
- **Labs:** global LB + regional backends; IAM least-privilege redesign.
- **Projects:**
  1. Reference global platform (GCLB, Cloud DNS, GKE, Cloud SQL/Spanner decision matrix).
     - Outcomes: 99.99% availability target, cross-region RTO < 15m.
  2. Hybrid connectivity blueprint (on-prem + cloud).
     - Outcomes: latency variance -30%, secure private connectivity.
- **Questions:**
  - “How do you structure IAM boundaries for platform vs product teams?”

### L) Staff-Level Leadership
**Why it matters:** L6 bar is impact through direction-setting and influence.

- **Resources:** Staff Engineer (Will Larson), Google re:Work resources, architecture review templates.
- **Labs:** run weekly design review; write one-page strategy memos.
- **Projects:**
  1. 3-year reliability and platform roadmap for 5+ teams.
     - Outcomes: shared OKRs adopted by all teams, roadmap confidence survey +25 pts.
  2. Legacy-to-modern migration program.
     - Outcomes: 80% workload migration with zero Sev-1 during cutovers.
- **Questions:**
  - “Describe a time you changed roadmap direction without authority.”

---

## 4) Weekly Mock Interview Structure (repeat all 52 weeks)

| Session | Duration | Focus | Rubric |
|---|---:|---|---|
| System Design Mock | 60m | large-scale backend/infra design | requirements, tradeoffs, bottlenecks, evolution plan |
| Infra Deep-Dive Mock | 45m | networking/k8s/linux failure analysis | root-cause speed, correctness, mitigation depth |
| Leadership/Behavioral Mock | 45m | L6 scope, conflict, influence, roadmap | ownership scope, clarity, stakeholder strategy |
| Debrief | 30m | gap analysis + action items | 3 strengths, 3 gaps, next-week drills |

**Scoring (1–4):** 1 weak, 2 mixed, 3 hire, 4 strong hire. Track trendline weekly.

---

## 5) 12-Month Execution Calendar (Detailed)

### Quarter 1 (Months 1-3): Foundations + Core Backend/Distributed
- Deliverables:
  - Networking/Linux troubleshooting playbook
  - Resilient backend service template
  - Mini consensus + sharding prototype
- Milestones:
  - Complete 3 architecture docs
  - 12 mock interviews
  - 2 production-grade projects shipped to internal sandbox/prod-lite

### Quarter 2 (Months 4-6): Kubernetes + Observability + SRE Program
- Deliverables:
  - Multi-cluster k8s reference stack
  - Telemetry platform with OTel
  - SLO/error-budget governance doc
- Milestones:
  - Incident game day cadence established
  - Alert volume reduced in pilot services

### Quarter 3 (Months 7-9): Delivery Automation + Perf + Security
- Deliverables:
  - GitOps release platform
  - Capacity model + load-testing harness
  - Zero-trust/authN-authZ baseline
- Milestones:
  - Canary automation live
  - Forecasting and p99 dashboards adopted

### Quarter 4 (Months 10-12): Cloud Architecture + Leadership + Interview Loops
- Deliverables:
  - GCP global architecture blueprint
  - Cross-team migration plan + risk matrix
  - Final portfolio: 6 projects with metrics, 12 design docs, 20+ mock scorecards
- Milestones:
  - 4 full interview loops with external peers
  - Polished “Staff impact narratives” for 8 common behavioral themes

---

## 6) Portfolio of Production-Grade Projects (showcase set)

1. **Global Active-Active Transaction Platform** (DistSys + Backend + Cloud)
   - Metrics: availability 99.95%+, RPO < 5s, failover < 60s.
2. **SLO-Driven Alerting Overhaul** (SRE + Observability)
   - Metrics: alert fatigue -50%, MTTR -35%.
3. **GitOps Progressive Delivery Platform** (IaC + Release Engineering)
   - Metrics: change failure rate -40%, rollback time < 5m.
4. **K8s Multi-Cluster Reliability Framework** (K8s + Networking)
   - Metrics: failover < 3m, capacity utilization +20%.
5. **Performance/Capacity Command Center** (Perf + Cost)
   - Metrics: p99 -60%, compute cost -20%.
6. **Zero-Trust Service Security Program** (Security + Infra)
   - Metrics: 100% mTLS, critical vuln window -70%.

---

## 7) Suggested Resource Stack (high signal)

- **Books:** DDIA, Google SRE + Workbook, Systems Performance, Staff Engineer.
- **Courses:** MIT 6.824, Kubernetes the Hard Way, GCP Architect learning paths, CKA/CKS prep labs.
- **Repos/Labs:** kubernetes/kubernetes, etcd-io/etcd, istio/istio, open-telemetry/opentelemetry-collector, envoyproxy/envoy, terraform/terraform.
- **Papers:** Spanner, Bigtable, Dynamo, Chubby, Borg/Omega/Kubernetes, Dapper.
- **Content channels:** Google Cloud Tech, CNCF talks, USENIX SREcon videos.

---

## 8) Interview Readiness Gates (must-hit)

- **By Month 4:** Explain DNS/TCP/TLS path and debug live packet traces.
- **By Month 6:** Lead an incident review and defend SLO policy with product leadership.
- **By Month 8:** Produce quant-backed perf/capacity plan (Little’s Law + autoscaling math).
- **By Month 10:** Present multi-region cloud architecture with IAM/risk tradeoffs.
- **By Month 12:** Consistently score >=3/4 across design, infra, and leadership mocks.

---

## 9) Weekly Template (copy/paste)

- **Mon:** theory deep dive + one-page synthesis
- **Tue:** lab + instrumentation
- **Wed:** design doc section + tradeoff table
- **Thu:** debugging scenario + RCA
- **Fri:** perf/security experiment + metrics
- **Sat:** project implementation + PR
- **Sun:** 3 mock interviews + debrief

**Weekly outputs:**
1. 1 design document increment
2. 1 runbook/postmortem
3. 1 measurable project improvement
4. 1 mock interview scorecard set

---

## 10) Folderized Topic Library (Easy Language + Capstone per Topic)

A complete folder-based learning library was added at:

- `docs/google_l6_roadmap/README.md`

It includes:
- One folder per major heading (A-L domains)
- Separate subfolders for each subtopic
- Easy layman-language explanation in each topic README
- A **Capstone Project (Knowledge Test)** section at the end of every topic
