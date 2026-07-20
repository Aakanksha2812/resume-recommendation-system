import React, { useState } from 'react';
import './App.css';

function App() {
  const [formData, setFormData] = useState({
    candidateName: '',
    candidateEmail: '',
  });
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [recommendations, setRecommendations] = useState(null);
  const [error, setError] = useState(null);
  const [submitted, setSubmitted] = useState(false);
  const [step, setStep] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile && selectedFile.type === 'application/pdf') {
      setFile(selectedFile);
    } else {
      alert('Please select a PDF file');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      alert('Please select a PDF file');
      return;
    }

    setLoading(true);
    setError(null);
    setRecommendations(null);
    setStep('Uploading resume...');

    try {
      const formPayload = new FormData();
      formPayload.append('candidateName', formData.candidateName);
      formPayload.append('candidateEmail', formData.candidateEmail);
      formPayload.append('file', file);

      const uploadRes = await fetch('http://localhost:8081/resume/upload', {
        method: 'POST',
        body: formPayload
      });

      if (!uploadRes.ok) throw new Error('Resume upload failed');

      setStep('Parsing skills from resume...');
      setSubmitted(true);
      setLoading(false);

      pollRecommendations(formData.candidateEmail);

    } catch (err) {
      setError('Something went wrong. Please try again.');
      setLoading(false);
    }
  };

  const pollRecommendations = (email) => {
    setLoading(true);
    let attempts = 0;
    const maxAttempts = 24;
    const steps = [
      'Parsing skills from resume...',
      'Searching matching jobs...',
      'AI is analyzing your profile...',
      'Generating explanation...',
      'Almost done...'
    ];

    const interval = setInterval(async () => {
      attempts++;
      setStep(steps[Math.min(Math.floor(attempts / 5), steps.length - 1)]);

      try {
        const res = await fetch(`http://localhost:8083/recommendations/${email}`);
        const data = await res.json();

        if (data && data.length > 0) {
          setRecommendations(data[data.length - 1]);
          setLoading(false);
          setStep('');
          clearInterval(interval);
        }

        if (attempts >= maxAttempts) {
          setLoading(false);
          setError('Taking too long. Please check back later.');
          clearInterval(interval);
        }
      } catch (err) {
        console.error('Polling error:', err);
      }
    }, 5000);
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
                <label>Resume (PDF only)</label>
                <div className="file-upload">
                  <input
                    type="file"
                    accept=".pdf"
                    onChange={handleFileChange}
                    id="file-input"
                    style={{ display: 'none' }}
                  />
                  <label htmlFor="file-input" className="file-label">
                    {file ? `✅ ${file.name}` : '📄 Choose PDF File'}
                  </label>
                </div>
                <small>Upload your resume in PDF format</small>
              </div>

              <button type="submit" disabled={loading || !file}>
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
                <p>{step}</p>
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
                  <p1>{recommendations.explanations}</p1>
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
                    setFile(null);
                    setFormData({ candidateName: '', candidateEmail: '' });
                  }}
                >
                  Upload Another Resume
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