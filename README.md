# RAG Service

A production-grade **Retrieval-Augmented Generation (RAG)** microservice.
Responds in any language, streams answers token-by-token, and embeds as a one-liner Web Component on any website.

## Configuration Reference

### variables

| Variable | Required | Default | Description                   |
|----------|----------|-------|-------------------------------|
| `STREAMX_OPENAI_API_KEY` | **✅ always** | — | OpenAI API key                |
| `streamx.blueprints.openai-rag-sink.chat-profile.name` | **✅ always** | `environment` | Chat profile environment name |
| `streamx.blueprints.openai-rag-sink.chat-profile.system-prompt` | **✅ always** | — | Chat profile system prompt    |
| `streamx.blueprints.openai-rag-sink.chat-profile.active` | **✅ always** | — | Chat profile activation flag  |
| `streamx.blueprints.openai-rag-sink.chat-profile.display-name` | recommended | — | Chat profile display name     |

### Example endpoint call

```bash
curl -X POST http://localhost/api/chat \
     -H "Content-Type: application/json" \
     -d '{
       "question":    "How do I return a product?"
     }'
```
