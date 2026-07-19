import React, { useState } from 'react';
import './App.css';

function App() {
  const [formData, setFormData] = useState({
    candidateName: '',
    candidateEmail: '',
    fileName: '',
    fileContent: ''
  });
  const [loading, setLoading] = useState(false);
  const [recommendations, setRecommendations] = useState(null);
  const [error, setError] = useState(null);
  const [submitted, setSubmitted] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setRecommendations(null);

    try {
      // Resume upload karo
      const params = new URLSearchParams(formData);
      const uploadRes = await fetch(
        `http://localhost:8081/resume/upload?${params}`,
        { method: 'POST' }
      );

      if (!uploadRes.ok) throw new Error('Resume upload failed');

      setSubmitted(true);
      setLoading(false);

      // Poll karo recommendations ke liye — Ollama time lagata hai
      pollRecommendations(formData.candidateEmail);

    } catch (err) {
      setError('Something went wrong. Please try again.');
      setLoading(false);
    }
  };

  const pollRecommendations = (email) => {
    setLoading(true);
    let attempts = 0;
    const maxAttempts = 20;

    const interval = setInterval(async () => {
      attempts++;
      try {
        const res = await fetch(
          `http://localhost:8083/recommendations/${email}`
        );
        const data = await res.json();

        if (data && data.length > 0) {
          setRecommendations(data[data.length - 1]); // latest recommendation
          setLoading(false);
          clearInterval(interval);
        }

        if (attempts >= maxAttempts) {
          setLoading(false);
          setError('Recommendation is taking too long. Please check back later.');
          clearInterval(interval);
        }
      } catch (err) {
        console.error('Polling error:', err);
      }
    }, 5000); // har 5 seconds mein check karo
  };

  return (
    <div className="app">
      <header className="header">
        <h1>🎯 Resume Recommendation System</h1>
        <p>Upload your resume and get top job matches powered by AI</p>
      </header>

      <main className="main">
        {!submitted ? (
          <div className="form-container">
            <h2>Upload Your Resume</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Full Name</label>
                <input
                  type="text"
                  name="candidateName"
                  placeholder="Aakanksha Shinde"
                  value={formData.candidateName}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  name="candidateEmail"
                  placeholder="aakanksha@gmail.com"
                  value={formData.candidateEmail}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>File Name</label>
                <input
                  type="text"
                  name="fileName"
                  placeholder="my_resume.pdf"
                  value={formData.fileName}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Skills & Experience</label>
                <textarea
                  name="fileContent"
                  placeholder="Java Spring Boot Kafka MongoDB Microservices Docker..."
                  value={formData.fileContent}
                  onChange={handleChange}
                  rows={4}
                  required
                />
                <small>Enter your skills and experience keywords</small>
              </div>

              <button type="submit" disabled={loading}>
                {loading ? 'Processing...' : 'Get Job Recommendations'}
              </button>
            </form>
          </div>
        ) : (
          <div className="results-container">
            <h2>Results for {formData.candidateName}</h2>

            {loading && (
              <div className="loading">
                <div className="spinner"></div>
                <p>AI is analyzing your profile and finding best matches...</p>
                <small>This may take 1-2 minutes</small>
              </div>
            )}

            {error && (
              <div className="error">
                <p>{error}</p>
                <button onClick={() => { setSubmitted(false); setError(null); }}>
                  Try Again
                </button>
              </div>
            )}

            {recommendations && (
              <div className="recommendations">
                <div className="profile-summary">
                  <h3>📋 Your Profile</h3>
                  <p><strong>Name:</strong> {recommendations.candidateName}</p>
                  <p><strong>Email:</strong> {recommendations.candidateEmail}</p>
                </div>

                <div className="matched-jobs">
                  <h3>🏢 Top Job Matches</h3>
                  {recommendations.matchedJobs.map((job, index) => (
                    <div key={index} className="job-card">
                      <span className="job-rank">#{index + 1}</span>
                      <p>{job}</p>
                    </div>
                  ))}
                </div>

                <div className="explanation">
                  <h3>🤖 AI Analysis</h3>
                  <p>{recommendations.explanation}</p>
                </div>

                <button
                  className="new-search"
                  onClick={() => {
                    setSubmitted(false);
                    setRecommendations(null);
                    setFormData({ candidateName: '', candidateEmail: '', fileName: '', fileContent: '' });
                  }}
                >
                  Search Again
                </button>
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}

export default App;